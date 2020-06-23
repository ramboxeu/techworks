package io.github.ramboxeu.techworks.client.widget;

import io.github.ramboxeu.techworks.common.api.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenHandler;

public class SlotWidget extends Widget {
    public SlotWidget(int x, int y) {
        super(x, y, 18, 18);
    }

    @Override
    public void render(int mouseX, int mouseY, Screen screen, MinecraftClient client, ScreenHandler container) {
        super.render(mouseX, mouseY, screen, client, container);
        
    }
}
