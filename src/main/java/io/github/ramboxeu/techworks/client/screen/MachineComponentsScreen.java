package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.common.container.machine.MachineComponentsContainer;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;

public class MachineComponentsScreen extends ContainerScreen<MachineComponentsContainer> {
    public MachineComponentsScreen(MachineComponentsContainer container) {
        super(container, container.getPlayerInventory(), new TranslatableText("container.techworks.machine_components"));
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {

    }
}
