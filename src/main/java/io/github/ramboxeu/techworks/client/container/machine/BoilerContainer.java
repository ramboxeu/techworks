package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.HeatDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.HeatingWidget;
import io.github.ramboxeu.techworks.client.screen.widget.display.FluidDisplayWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

import java.util.List;
import java.util.Map;

public class BoilerContainer extends BaseMachineContainer<BoilerTile> {

    public BoilerContainer(int id, PlayerInventory playerInventory, BoilerTile tile, Map<Side, List<HandlerConfig>> dataMap) {
        super(TechworksContainers.BOILER.get(), id, playerInventory, tile, dataMap);

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
