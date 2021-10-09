package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import io.github.ramboxeu.techworks.common.util.Predicates;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SteamEngineContainer extends BaseMachineContainer<SteamEngineTile> {
    public SteamEngineContainer(int id, PlayerInventory playerInventory, SteamEngineTile machineTile) {
        super(TechworksContainers.STEAM_ENGINE.get(), id, playerInventory, machineTile);
    }

//    @Override
//    protected void layoutSlots(InventoryBuilder builder) {
//        builder.addSlot(new SlotBuilder(27, 16).limit(1).predicate(itemStack -> false))
//                .addSlot(new SlotBuilder(27, 54).limit(1).predicate(itemStack -> false))
//                .addSlot(new SlotBuilder(133, 35).limit(1).predicate(PredicateUtils::isEnergyStorage));
//    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack slotItemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null) {
            slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();
            if (index < 36) {
                if (Predicates.isEnergyStorage(itemStack)) {
                    Slot energyOutput = this.inventorySlots.get(37);

                    ItemStack itemStack1 = ItemStack.EMPTY;

                    itemStack1 = itemStack.split(1);

                    if (!energyOutput.getHasStack() && energyOutput.isItemValid(itemStack)) {
                        energyOutput.putStack(itemStack1);
                        slot.putStack(itemStack);
                        return ItemStack.EMPTY;
                    }
                }

                // TODO: Add gas support
            } else {
                if (!this.mergeItemStack(itemStack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        slot.putStack(itemStack);

        return ItemStack.EMPTY;
    }
}
