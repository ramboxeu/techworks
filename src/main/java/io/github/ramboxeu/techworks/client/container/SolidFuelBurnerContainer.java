package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.client.screen.widget.HeatDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.BurningProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.SolidFuelBurnerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;

public class SolidFuelBurnerContainer extends BaseInventoryContainer {

    public SolidFuelBurnerContainer(int id, PlayerInventory playerInv, SolidFuelBurnerTile tile) {
        super(TechworksContainers.SOLID_FUEL_BURNER.get(), playerInv, id);

        layoutPlayerSlots();

        addWidget(new BurningProgressWidget(81, 37, true, tile::getBurnTime, tile::getElapsedTime));
        addWidget(new HeatDisplayWidget(163, 15, 150, tile::getStoredHeat));
        addSlot(new SlotItemHandler(tile.getFuelInv(), 0, 80, 53));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}
