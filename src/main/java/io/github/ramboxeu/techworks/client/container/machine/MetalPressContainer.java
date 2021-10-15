package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.ArrowProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class MetalPressContainer extends BaseMachineContainer<MetalPressTile> {
    public MetalPressContainer(int id, PlayerInventory playerInventory, MetalPressTile tile) {
        super(TechworksContainers.METAL_PRESS.get(), id, playerInventory, tile);

        addWidget(new EnergyDisplayWidget(this, 7, 14, tile.getBatteryData()));
        addWidget(new SlotWidget(this, 55, 34, 0, false, tile.getInvData()));
        addWidget(new SlotWidget(this, 111, 30, 0, true, tile.getOutputInvData()));
        addWidget(new ArrowProgressWidget(81, 35, false, () -> 4000, tile::getExtractedEnergy));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
