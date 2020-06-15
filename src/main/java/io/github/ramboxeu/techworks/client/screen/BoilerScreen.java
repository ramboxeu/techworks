package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.text.TranslatableText;

public class BoilerScreen extends ContainerScreen<BoilerContainer> {
    public BoilerScreen(BoilerContainer container) {
        super(container, container.getPlayerInventory(), new TranslatableText("screen.techworks.boiler"));
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {

    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        font.draw(String.valueOf(container.getDummyInt()), 10, 10, 4210752);
    }
}
