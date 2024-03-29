package io.github.ramboxeu.techworks.client.screen.widget.display;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.container.sync.FluidStackHolder;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.IPortScreenWidgetProvider;
import io.github.ramboxeu.techworks.client.screen.widget.PortContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.PortScreenWidget;
import io.github.ramboxeu.techworks.client.util.Color;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import io.github.ramboxeu.techworks.common.util.machineio.data.FluidHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class FluidDisplayWidget extends PortContainerWidget implements IPortScreenWidgetProvider<FluidDisplayWidget.ScreenWidget> {
    private int x;
    private int y;
    private int width;
    private int height;
    private int capacity;
    private ScreenWidget screenWidgetInstance;
    private FluidStack fluid;

    private final IFluidTank handler;
    private final FluidHandlerData data;

    public FluidDisplayWidget(BaseMachineContainer<?> container, int x, int y, int width, int height, FluidHandlerData data) {
        super(container, data);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.data = data;

        handler = data.getFluidTank();
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        builder.track(new FluidStackHolder(handler::getFluid, value -> {
            fluid = value;
            screenWidgetInstance.updateFluid(fluid, capacity);
        }));

        builder.track(new IntReferenceHolder() {
            @Override
            public int get() {
                return handler.getCapacity();
            }

            @Override
            public void set(int value) {
                capacity = value;
                screenWidgetInstance.updateFluid(fluid, value);
            }
        });
    }

    @Override
    public ScreenWidget getPortScreenWidget(BaseMachineScreen<?, ?> screen) {
        if (screenWidgetInstance == null) {
            screenWidgetInstance = new ScreenWidget(screen, x, y, width, height, data);
            screenWidgetInstance.updateFluid(fluid, capacity);
        }

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends PortScreenWidget {
        private Color color;
        private ResourceLocation tex;
        private float progress;
        private int capacity;

        private FluidStack fluid;

        public ScreenWidget(BaseMachineScreen<?, ?> screen, int x, int y, int width, int height, HandlerData data) {
            super(screen, x, y, width, height, data);
        }

        @Override
        protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            if (fluid != null && !fluid.isEmpty()) {
                stack.push();
                int tankHeight = Math.round((height - 2) * progress);
                int tankY = y + 1 + ((height - 2) - tankHeight);

                RenderUtils.drawFluid(stack, x + 1, tankY, width - 2, tankHeight, color, tex);
                stack.pop();
            }

            super.renderBaseWidget(stack, mouseX, mouseY, partialTicks);
        }

        @Override
        protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {
            renderTooltip(stack, getFluidName(fluid), mouseX, mouseY);
        }

        public ITextComponent getFluidName(FluidStack stack) {
            if (fluid != null) {
                if (!fluid.isEmpty()) {
                    return TranslationKeys.STORED_FLUID.text(
                            new TranslationTextComponent(stack.getFluid().getAttributes().getTranslationKey(stack)),
                            fluid.getAmount(),
                            capacity
                    );
                }
            }

            return TranslationKeys.STORED_FLUID.text(TranslationKeys.EMPTY.text(), 0, capacity);
        }

        public void updateFluid(FluidStack fluid, int capacity) {
            if (fluid != null) {
                FluidAttributes attributes = fluid.getFluid().getAttributes();

                color = Color.fromRGBA(attributes.getColor(fluid));
                tex = attributes.getStillTexture(fluid);
                progress = MathUtils.calcProgress(fluid.getAmount(), capacity);
            }

            this.fluid = fluid;
            this.capacity = capacity;
        }
    }
}
