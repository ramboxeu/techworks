package io.github.ramboxeu.techworks.client.screen.widget.display;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.IPortScreenWidgetProvider;
import io.github.ramboxeu.techworks.client.screen.widget.PortContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.PortScreenWidget;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyDisplayWidget extends PortContainerWidget implements IPortScreenWidgetProvider<EnergyDisplayWidget.ScreenWidget> {
    private int x;
    private int y;
    private int width;
    private int height;
    private int capacity;
    private int energy;

    private final IEnergyStorage handler;
    private final HandlerData data;

    private ScreenWidget screenWidgetInstance;

    public EnergyDisplayWidget(BaseMachineContainer<?> container, int x, int y, EnergyHandlerData data) {
        this(container, x, y, 18, 56, data);
    }

    public EnergyDisplayWidget(BaseMachineContainer<?> container, int x, int y, int width, int height, EnergyHandlerData data) {
        super(container, data);
        this.x = x;
        this.y = y;
        this.data = data;
        this.width = width;
        this.height = height;

        handler = data.getHandler();
        capacity = handler.getMaxEnergyStored();
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        builder.track(new IntReferenceHolder() {
            @Override
            public int get() {
                return handler.getEnergyStored();
            }

            @Override
            public void set(int value) {
                energy = value;
                screenWidgetInstance.fillRatio = MathUtils.calcProgress(value, capacity);
            }
        });

        builder.track(new IntReferenceHolder() {
            @Override
            public int get() {
                return handler.getMaxEnergyStored();
            }

            @Override
            public void set(int value) {
                capacity = value;
            }
        });
    }

    @Override
    public ScreenWidget getPortScreenWidget(BaseMachineScreen<?, ?> screen) {
        if (screenWidgetInstance == null) {
            screenWidgetInstance = new ScreenWidget(screen, x, y, width, height, data, this);
        }

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends PortScreenWidget {
        private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/battery.png");

        private final EnergyDisplayWidget widget;
        private float fillRatio;

        public ScreenWidget(BaseMachineScreen<?, ?> screen, int x, int y, int width, int height, HandlerData data, EnergyDisplayWidget widget) {
            super(screen, x, y, width, height, data);

            this.widget = widget;
        }

        @Override
        protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            int height = Math.round(this.height * fillRatio);
            int texHeight = this.height - height;

            minecraft.textureManager.bindTexture(TEX);
            blit(stack, x + 1, y + texHeight, width - 2, height, 18, texHeight, 16, height, 34, 56);

            super.renderBaseWidget(stack, mouseX, mouseY, partialTicks);
        }

        @Override
        protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {
            screen.renderTooltip(stack, widget.energy + "/" + widget.capacity + "FE", mouseX, mouseY);
        }
    }
}
