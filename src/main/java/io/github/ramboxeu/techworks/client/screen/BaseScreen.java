package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.screen.widget.BaseWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

import java.util.ArrayList;
import java.util.List;

// Just a ContainerScreen with some probably hacky hacks
public class BaseScreen<T extends Container> extends ContainerScreen<T> {
    protected ResourceLocation background;
    protected List<BaseWidget> widgets = new ArrayList<>();

    public BaseScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, ResourceLocation background) {
        super(screenContainer, inv, titleIn);

        this.background = background;
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.textureManager.bindTexture(background);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        blit(stack, x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        for (Widget widget : widgets) {
            widget.render(stack, mouseX, mouseY, Minecraft.getInstance().getRenderPartialTicks());
        }

        renderHoveredTooltip(stack, mouseX, mouseY);

        super.func_230451_b_(stack, mouseX, mouseY);
    }

    protected void renderHoveredTooltip(MatrixStack stack, int mouseX, int mouseY) {
        for (BaseWidget widget : widgets) {
            if (widget.isMouseOver(mouseX - guiLeft, mouseY - guiTop)) {
                widget.renderTooltip(stack, mouseX - guiLeft, mouseY - guiTop, width, height);
            }
        }
    }

    protected void renderTooltip(MatrixStack stack, String text, int mouseX, int mouseY) {
        renderTooltip(stack, ITextProperties.func_240652_a_(text), mouseX, mouseY);
    }

    protected <T extends BaseWidget> T addWidget(T widget) {
        this.widgets.add(widget);
        addListener(widget);
        return widget;
    }
}
