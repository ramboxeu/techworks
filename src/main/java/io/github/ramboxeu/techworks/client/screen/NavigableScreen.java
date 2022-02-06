package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class NavigableScreen extends Screen {
    protected final ResourceLocation background;

    protected Screen previous;
    protected Button back;
    protected int titleX = 24;
    protected int titleY = 6;
    protected int guiWidth = ClientUtils.GUI_WIDTH;
    protected int guiHeight = ClientUtils.GUI_HEIGHT;
    protected int guiLeft;
    protected int guiTop;

    public NavigableScreen(ITextComponent title, Screen previous, ResourceLocation background) {
        super(title);
        this.previous = previous;
        this.background = background;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack, mouseX, mouseY, partialTicks);
        super.render(stack, mouseX, mouseY, partialTicks);
        stack.push();
        stack.translate(guiLeft, guiTop, 0);
        renderForeground(stack, mouseX, mouseY, partialTicks);
        stack.pop();
    }

    @Override
    protected void init() {
        guiLeft = (width - guiWidth) / 2;
        guiTop = (height - guiHeight) / 2;

        back = new BackButton(guiLeft + 8, guiTop + 4, 11, 11, StringTextComponent.EMPTY, this::onGoBackPressed, this::onGoBackTooltip);
        addButton(back);
    }

    protected void renderBackground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        minecraft.textureManager.bindTexture(background);
        blit(stack, guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
    }

    protected void renderForeground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        ClientUtils.drawString(stack, font, title, titleX, titleY, 0x404040, false);
    }

    protected void onGoBackPressed(Button button) {
        ClientUtils.changeCurrentScreen(this, previous, true);
    }

    protected void onGoBackTooltip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        renderTooltip(stack, TranslationKeys.GO_BACK.text(), mouseX, mouseY);
    }

    public static class BackButton extends Button {

        public BackButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip tooltipAction) {
            super(x, y, width, height, title, pressedAction, tooltipAction);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            Minecraft mc = Minecraft.getInstance();
            mc.textureManager.bindTexture(ClientUtils.WIDGETS_TEX);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            int texX = isHovered() ? 11 : 0;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            blit(stack, x, y, texX, 18, 11, 11, 32, 32);

            if (this.isHovered()) {
                this.renderToolTip(stack, mouseX, mouseY);
            }
        }
    }
}
