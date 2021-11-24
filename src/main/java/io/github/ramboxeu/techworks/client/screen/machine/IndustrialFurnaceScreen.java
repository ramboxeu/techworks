package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.IndustrialFurnaceContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.IndustrialFurnaceTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class IndustrialFurnaceScreen extends BaseMachineScreen<IndustrialFurnaceTile, IndustrialFurnaceContainer> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/industrial_furnace.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/industrial_furnace_front_off");

    public IndustrialFurnaceScreen(IndustrialFurnaceContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, TEXTURE, MACHINE_FRONT);
    }
}
