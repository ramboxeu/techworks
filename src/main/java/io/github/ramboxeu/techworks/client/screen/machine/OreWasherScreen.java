package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.OreWasherContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.OreWasherTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OreWasherScreen extends BaseMachineScreen<OreWasherTile, OreWasherContainer> {
    public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/ore_washer.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/ore_washer_front_off");

    public OreWasherScreen(OreWasherContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, TEX, MACHINE_FRONT);
    }


}
