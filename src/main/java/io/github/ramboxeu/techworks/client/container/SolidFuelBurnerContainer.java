package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.client.screen.widget.progress.BurningProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.SolidFuelBurnerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.items.SlotItemHandler;

public class SolidFuelBurnerContainer extends BaseInventoryContainer {
    private final SolidFuelBurnerTile tile;
    private String heatText = "Heat: ???HU";

    public SolidFuelBurnerContainer(int id, PlayerInventory playerInv, SolidFuelBurnerTile tile) {
        super(TechworksContainers.SOLID_FUEL_BURNER.get(), playerInv, id);
        this.tile = tile;

        layoutPlayerSlots();

        addWidget(new BurningProgressWidget(81, 37, true, tile::getBurnTime, tile::getElapsedTime));
        addSlot(new SlotItemHandler(tile.getFuelInv(), 0, 80, 53));

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.getStoredHeat();
            }

            @Override
            public void set(int value) {
                heatText = "Heat: " + value + " HU";
            }
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    public String getHeatText() {
        return heatText;
    }
}
