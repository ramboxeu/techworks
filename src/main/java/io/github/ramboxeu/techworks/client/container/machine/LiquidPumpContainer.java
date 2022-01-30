package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.display.FluidDisplayWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.LiquidPumpTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class LiquidPumpContainer extends BaseMachineContainer<LiquidPumpTile> {

    public LiquidPumpContainer(int id, PlayerInventory playerInventory, LiquidPumpTile tile) {
        super(TechworksContainers.LIQUID_PUMP.get(), id, playerInventory, tile);

        addWidget(new FluidDisplayWidget(this, 151, 14, 18, 56, tile.getTankData()));
        addWidget(new EnergyDisplayWidget(this, 7, 14, tile.getBatteryData()));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
