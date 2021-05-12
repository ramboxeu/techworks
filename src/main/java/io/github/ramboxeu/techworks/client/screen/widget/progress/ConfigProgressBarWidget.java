package io.github.ramboxeu.techworks.client.screen.widget.progress;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.client.screen.widget.BaseScreenWidget;

public class ConfigProgressBarWidget extends BaseScreenWidget {

    private float progress;

    public ConfigProgressBarWidget(BaseScreen<?> screen, int x, int y, int width, int height) {
        super(screen, x, y, width, height);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        hLine(stack, x, x + width, y, 0x474545);
        hLine(stack, x, x + width, (y + height) - 1, 0x474545);
        vLine(stack, x, y, y + height, 0x474545);
        vLine(stack, (x + height) - 1, y, y + height, 0x474545);

        int barWidth = (int) ((width - 2) * progress);

        fill(stack, x, y, x + barWidth, (y + height) - 2, 0x2D9938);
        fill(stack, x, y, x + (width - barWidth), (y + height) - 2, 0x2B2828);
    }
}
