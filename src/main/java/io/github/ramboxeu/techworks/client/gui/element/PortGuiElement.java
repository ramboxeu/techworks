package io.github.ramboxeu.techworks.client.gui.element;

public abstract class PortGuiElement extends GuiElement {
    protected final int color;

    public PortGuiElement(int x, int y, int width, int height, int color) {
        super(x, y, width, height);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
