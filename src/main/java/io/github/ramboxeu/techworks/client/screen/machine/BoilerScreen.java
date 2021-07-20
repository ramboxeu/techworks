package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BoilerScreen extends BaseMachineScreen<BoilerTile, BoilerContainer> {
    public static final ResourceLocation BOILER_GUI_TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/boiler.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/electric_furnace_front_off");

    public BoilerScreen(BoilerContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title, BOILER_GUI_TEXTURE, MACHINE_FRONT);
    }
}
