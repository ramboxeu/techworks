package io.github.ramboxeu.techworks.client.screen.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.LiquidPumpContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.common.tile.machine.LiquidPumpTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LiquidPumpScreen extends BaseMachineScreen<LiquidPumpTile, LiquidPumpContainer> {
    public static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/container/liquid_pump.png");
    public static final ResourceLocation MACHINE_FRONT = new ResourceLocation(Techworks.MOD_ID, "block/liquid_pump_front_off");

    public LiquidPumpScreen(LiquidPumpContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, TEX, MACHINE_FRONT);
    }
}
