package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.screen.widget.BaseContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.BaseScreenWidget;
import io.github.ramboxeu.techworks.client.screen.widget.IScreenWidgetProvider;
import io.github.ramboxeu.techworks.client.util.ClientUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseScreen<T extends BaseContainer> extends ContainerScreen<T> {
    protected ResourceLocation background;
    protected List<BaseScreenWidget> widgets = new ArrayList<>();
    protected final T container;

    public BaseScreen(T container, PlayerInventory inv, ITextComponent titleIn, ResourceLocation background) {
        super(container, inv, titleIn);

        this.container = container;
        this.background = background;

        for (BaseContainerWidget widget : container.getWidgets()) {
            if (widget instanceof IScreenWidgetProvider<?>) {
                addWidget(((IScreenWidgetProvider<?>) widget).getScreenWidget(this));
            }
        }
    }

    protected <T extends BaseScreenWidget> T addWidget(T widget) {
        if (!widgets.contains(widget)) {
            widgets.add(widget);
        }

        return widget;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.textureManager.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        blit(stack, x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(stack, mouseX, mouseY);
        super.renderHoveredTooltip(stack, mouseX - guiLeft, mouseY - guiTop);
    }

    @Override
    protected void init() {
        super.init();
        buttons.addAll(widgets);
        children.addAll(widgets);
        reInitWidgets();
    }

    protected void reInitWidgets() {
        for (BaseScreenWidget widget : widgets) {
            widget.onScreenInit(minecraft, guiLeft, guiTop, xSize, ySize);
        }
    }

    public void renderTooltip(MatrixStack stack, String text, int mouseX, int mouseY) {
        renderTooltip(stack, Collections.singletonList(ClientUtils.processor(text)), mouseX, mouseY);
    }

    public static ResourceLocation guiTexture(String name) {
        return new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/" + name + ".png");
    }
}
