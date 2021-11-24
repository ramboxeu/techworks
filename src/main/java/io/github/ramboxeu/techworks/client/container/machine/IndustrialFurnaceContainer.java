package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.HeatDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.ArrowProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.IndustrialFurnaceTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class IndustrialFurnaceContainer extends BaseMachineContainer<IndustrialFurnaceTile> {
    public IndustrialFurnaceContainer(int id, PlayerInventory inventory, IndustrialFurnaceTile tile) {
        super(TechworksContainers.INDUSTRIAL_FURNACE.get(), id, inventory, tile);

        addWidget(new HeatDisplayWidget(163, 15, 1000, tile::getHeat, tile::getTemperature));
        addWidget(new SlotWidget(this, 37, 34, 0, false, tile.getInvPrimaryData()));
        addWidget(new SlotWidget(this, 55, 34, 1, false, tile.getInvSecondaryData()));
        addWidget(new SlotWidget(this, 111, 30, 0, true, tile.getOutputInvData()));
        addWidget(new ArrowProgressWidget(81, 35, false, tile::getRequiredHeat, tile::getExtractedHeat));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
