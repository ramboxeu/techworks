package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public abstract class BaseScreenWidget extends Widget {
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

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        int mx = mouseX - guiLeft;
        int my = mouseY - guiTop;
        isHovered = mx >= x && my >= y && mx <= x + width && my <= y + height;

        stack.push();
        stack.translate(guiLeft, guiTop, 0);
        renderBaseWidget(stack, mouseX, mouseY, partialTicks);
        stack.pop();
    }

    @Override
    public void renderToolTip(MatrixStack stack, int mouseX, int mouseY) {
        renderWidgetTooltip(stack, (mouseX - guiLeft), (mouseY - guiTop));
    }

    protected void renderTooltip(MatrixStack stack, List<? extends ITextProperties> tooltip, int mouseX, int mouseY) {
        ClientUtils.renderTooltip(screen, minecraft.fontRenderer, stack, tooltip, mouseX + guiLeft, mouseY + guiTop);
    }

    protected void renderTooltip(MatrixStack stack, ITextProperties tooltip, int mouseX, int mouseY) {
        ClientUtils.renderTooltip(screen, minecraft.fontRenderer, stack, tooltip, mouseX + guiLeft, mouseY + guiTop);
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
