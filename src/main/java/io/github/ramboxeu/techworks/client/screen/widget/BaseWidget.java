package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public abstract class BaseWidget extends Widget {
    public BaseWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new StringTextComponent(""));
    }

    // Somewhat hacky way of rendering tooltips, until something with matrices transformations will be made
    public void renderTooltip(MatrixStack stack, int mouseX, int mouseY, int width, int height) {}


}
