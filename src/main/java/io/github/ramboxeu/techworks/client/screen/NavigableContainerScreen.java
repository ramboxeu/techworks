package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class NavigableContainerScreen<T extends Container> extends ContainerScreen<T> {
    protected final ResourceLocation background;
    protected Screen previous;
    protected Button back;

    public NavigableContainerScreen(T container, PlayerInventory inv, ITextComponent title, Screen previous, ResourceLocation background) {
        super(container, inv, title);
        this.previous = previous;
        this.background = background;

        titleX = 24;
        titleY = 6;
    }

    @Override
    public void onClose() {}

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        minecraft.textureManager.bindTexture(background);
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void init() {
        super.init();
        back = new NavigableScreen.BackButton(guiLeft + 8, guiTop + 4, 11, 11, StringTextComponent.EMPTY, this::onGoBackPressed, this::onGoBackTooltip);
        addButton(back);
    }

    protected void onGoBackPressed(Button button) {
        ClientUtils.changeCurrentScreen(this, previous, true);
    }

    protected void onGoBackTooltip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        renderTooltip(stack, TranslationKeys.GO_BACK.text(), mouseX, mouseY);
    }
}
