package io.github.ramboxeu.techworks.client.gui.element;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;

public abstract class GuiElement extends AbstractGui implements IRenderable, IGuiEventListener {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Minecraft minecraft;
    protected BaseScreen<?> screen;
    protected BaseContainer container;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void init(BaseScreen<?> screen) {
        this.minecraft = screen.getMinecraft();
        this.screen = screen;
    }

    public void init(BaseContainer container) {
        this.container = container;
    }

    /* // Called client side only (by screens)
     * public ScreenElement getScreenElement( params ) {}
     */

    /*
     * public static class ScreenElement extends AbstractGui implements IRenderable, IGuiEventListener {}
     */
}
