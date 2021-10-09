package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.display.FluidDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotGroupWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.ArrowProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.OreWasherTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class OreWasherContainer extends BaseMachineContainer<OreWasherTile> {

    public OreWasherContainer(int id, PlayerInventory playerInv, OreWasherTile tile) {
        super(TechworksContainers.ORE_WASHER.get(), id, playerInv, tile);

        addWidget(new FluidDisplayWidget(this, 151, 14, 18, 56, tile.getWaterTankData()));
        addWidget(new EnergyDisplayWidget(this, 7, 14, tile.getBatteryData()));
        addWidget(new SlotWidget(this, 51, 34, 0, false, tile.getInvData()));
        addWidget(new SlotGroupWidget(this, 107, 26, 2, 2, tile.getOutputInvData()));
        addWidget(new ArrowProgressWidget(77, 35, false, () -> 4000, tile::getExtractedEnergy));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
