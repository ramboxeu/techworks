package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;

public abstract class PortScreenWidget extends BaseScreenWidget {
    private final int color;
    protected final BaseMachineScreen<?, ?> machineScreen;
    protected HandlerData data;

    public PortScreenWidget(BaseMachineScreen<?, ?> screen, int x, int y, int width, int height, HandlerData data) {
        super(screen, x, y, width, height);
        this.data = data;
        this.machineScreen = screen;
        color = data.getColor();
    }

    public int getColor() {
        return color;
    }

    @Override
    protected void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (machineScreen.getRenderConfig()) {
            int x = this.x - 1;
            int y = this.y - 1;

            fill(stack, x, y, x + width + 2, y + 1, color);
            fill(stack, x, y + 1, x + 1, y + height + 1, color);
            fill(stack, x + width + 1, y + 1, x + width + 2, y + height + 1, color);
            fill(stack, x, y + height + 1, x + width + 2, y + height + 2, color);
        }
    }
}
