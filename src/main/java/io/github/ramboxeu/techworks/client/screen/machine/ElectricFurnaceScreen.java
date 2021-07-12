package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricFurnaceScreen extends BaseMachineScreen<ElectricFurnaceTile, ElectricFurnaceContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/electric_furnace.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/electric_furnace_front_off");

    public ElectricFurnaceScreen(ElectricFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn, TEXTURE, MACHINE_FRONT);
    }
}
