package io.github.ramboxeu.techworks.client.screen.widget.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.IPortScreenWidgetProvider;
import io.github.ramboxeu.techworks.client.screen.widget.PortContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.PortScreenWidget;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotGroupWidget extends PortContainerWidget implements IPortScreenWidgetProvider<SlotGroupWidget.ScreenWidget> {
    private int x;
    private int y;
    private int rows;
    private int cols;
    private final IItemHandler handler;
    private ScreenWidget screenWidgetInstance;
    private final SlotFactory factory;

    public SlotGroupWidget(BaseMachineContainer<?> container, int x, int y, int rows, int cols, ItemHandlerData data) {
        this(container, x, y, rows, cols, data, SlotItemHandler::new);
    }

    public SlotGroupWidget(BaseMachineContainer<?> container, int x, int y, int rows, int cols, ItemHandlerData data, SlotFactory factory) {
        super(container, data);
        this.x = x;
        this.y = y;
        this.rows = rows;
        this.cols = cols;
        this.factory = factory;

        handler = data.getHandler();
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
//                builder.slot(factory.create(handler, i + j * cols, x + (i * 17 + (i == 0 ? 0 : 1)), y + (j * 17 + (j == 0 ? 0 : 1))));
                builder.slot(factory.create(handler, i + j * cols, x + (18 * i), y + (18 * j)));
            }
        }
    }

    @Override
    public ScreenWidget getPortScreenWidget(BaseMachineScreen<?, ?> screen) {
        if (screenWidgetInstance == null) {
            screenWidgetInstance = new ScreenWidget(screen, x - 1, y - 1, rows, cols, data);
        }

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends PortScreenWidget {
        private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/slot/component.png");
        private int rows;
        private int cols;

        public ScreenWidget(BaseMachineScreen<?, ?> screen, int x, int y, int rows, int cols, HandlerData data) {
            super(screen, x, y, cols * 18, rows * 18, data);

            this.rows = rows;
            this.cols = cols;
        }

        @Override
        protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            super.renderBaseWidget(stack, mouseX, mouseY, partialTicks);
//
//            minecraft.textureManager.bindTexture(TEX);
//            for (int i = 0; i < rows; i++) {
//                for (int j = 0; j < cols; j++) {
//                    blit(stack, x + (i * 18), y + (j * 18), 0, 0, 0, 18, 18, 18, 18);
//                }
//            }
        }
    }
}
