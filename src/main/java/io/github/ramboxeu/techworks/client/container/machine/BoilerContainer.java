package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.HeatDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.HeatingWidget;
import io.github.ramboxeu.techworks.client.screen.widget.display.FluidDisplayWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class BoilerContainer extends BaseMachineContainer<BoilerTile> {

    public BoilerContainer(int id, PlayerInventory playerInventory, BoilerTile tile) {
        super(TechworksContainers.BOILER.get(), id, playerInventory, tile);

        addWidget(new FluidDisplayWidget(this, 7, 14, 18, 56, tile.getWaterTankData()));
        addWidget(new FluidDisplayWidget(this, 151, 14, 18, 56, tile.getSteamTankData()));
        addWidget(new HeatingWidget(tile.getHeater(), 79, 36, 29, 14, 29, 14));
        addWidget(new HeatDisplayWidget(141, 14, 1000, tile::getTemperature));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
