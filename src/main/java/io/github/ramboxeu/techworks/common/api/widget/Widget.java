package io.github.ramboxeu.techworks.common.api.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.Container;

import java.util.Collections;
import java.util.List;

public abstract class Widget {
    private int x;
    private int y;
    private int width;
    private int height;
    protected Screen screen;
    protected MinecraftClient client;
    protected Container container;

    public Widget(int x, int y, int width, int height) {}

    public void render(int mouseX, int mouseY, Screen screen, MinecraftClient client, Container container) {}

    public List<String> buildTooltip(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}