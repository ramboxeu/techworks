package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public abstract class BaseScreenWidget extends Widget {
//    protected int x;
//    protected int y;
//    protected int width;
//    protected int height;
    protected final BaseScreen<?> screen;

    protected Minecraft minecraft;
    protected int guiLeft;
    protected int guiTop;
    protected int guiWidth;
    protected int guiHeight;

    public BaseScreenWidget(BaseScreen<?> screen, int x, int y, int width, int height) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

//    protected boolean isMouseInPoint(int mouseX, int mouseY, int pointX, int pointY) {
//        int mx = mouseX - guiLeft;
//        int my = mouseY - guiTop;
//
//        return
//    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int mx = mouseX - guiLeft;
        int my = mouseY - guiTop;
        isHovered = mx >= x && my >= y && mx <= x + width && my <= y + height;

        stack.push();
        stack.translate(guiLeft, guiTop, 0);
        renderBaseWidget(stack, mouseX, mouseY, partialTicks);

        if (this.isHovered()) {
            renderToolTip(stack, mouseX, mouseY);
        }

        stack.pop();
    }

    @Override
    public void renderToolTip(MatrixStack stack, int mouseX, int mouseY) {
        renderWidgetTooltip(stack, (mouseX - guiLeft), (mouseY - guiTop));
    }

    // stack is adjusted to guLeft, guiTop
    protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {}

    // mouseX, mouseY are adjusted
    protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {}

    // Called in Screen#init
    public void onScreenInit(Minecraft minecraft, int guiLeft, int guiTop, int guiWidth, int guiHeight) {
        this.minecraft = minecraft;
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
    }
}
