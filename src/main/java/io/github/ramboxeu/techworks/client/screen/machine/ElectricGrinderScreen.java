package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.ElectricGrinderContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricGrinderTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricGrinderScreen extends BaseMachineScreen<ElectricGrinderTile, ElectricGrinderContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/electric_grinder.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/electric_grinder_front_off");

    public ElectricGrinderScreen(ElectricGrinderContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title, TEXTURE, MACHINE_FRONT);
    }
}
