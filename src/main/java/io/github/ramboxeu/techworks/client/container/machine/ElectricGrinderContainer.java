package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.client.screen.widget.inventory.SlotWidget;
import io.github.ramboxeu.techworks.client.screen.widget.progress.ArrowProgressWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricGrinderTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ElectricGrinderContainer extends BaseMachineContainer<ElectricGrinderTile> {

    public ElectricGrinderContainer(int id, PlayerInventory playerInventory, ElectricGrinderTile tile) {
        super(TechworksContainers.ELECTRIC_GRINDER.get(), id, playerInventory, tile);

        addWidget(new EnergyDisplayWidget(this, 8, 14, tile.getBatteryData()));
        addWidget(new SlotWidget(this, 55, 34, 0, false, tile.getInvData()));
        addWidget(new SlotWidget(this, 111, 30, 0, true, tile.getOutputInvData(),
                (handler, index, x, y) -> new SlotItemHandler(handler, index, x, y) {
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack) {
                        return false;
                    }
                })
        );
        addWidget(new ArrowProgressWidget(81, 35, false, tile::getEnergy, tile::getExtractedEnergy));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
