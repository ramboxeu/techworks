package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricGrinderTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class ElectricGrinderContainer extends BaseMachineContainer<ElectricGrinderTile> {
    private int workTime;
    private int workCounter;

    public ElectricGrinderContainer(int id, PlayerInventory playerInventory, ElectricGrinderTile machineTile, Map<Side, List<HandlerConfig>> dataMap) {
        super(TechworksContainers.ELECTRIC_GRINDER.getContainer(), id, playerInventory, machineTile, dataMap);

//        this.trackInt(new IntReferenceHolder() {
//            @Override
//            public int get() {
//                return machineTile.getOperationTime();
//            }
//
//            @Override
//            public void set(int time) {
//                workTime = time;
//            }
//        });
//
//        this.trackInt(new IntReferenceHolder() {
//            @Override
//            public int get() {
//                return machineTile.getTimeCounter();
//            }
//
//            @Override
//            public void set(int counter) {
//                workCounter = counter;
//            }
//        });
    }

//    @Override
//    protected void layoutSlots(InventoryBuilder builder) {
//        builder.addSlot(new SlotBuilder(56, 35))
//                .addSlot(new SlotBuilder(32, 53).predicate(PredicateUtils::isEnergyStorage))
//                .addSlot(new SlotBuilder(116, 35).output(true).limit(0));
//    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public int getWorkTime() {
        return this.workTime;
    }

    public int getWorkCounter() {
        return this.workCounter;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack slotItemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null) {
            slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();
            if (index < 36) {
                if (PredicateUtils.isEnergyStorage(itemStack)) {
                    Slot energyOutput = this.inventorySlots.get(37);

                    ItemStack itemStack1 = ItemStack.EMPTY;

                    itemStack1 = itemStack.split(1);

                    if (!energyOutput.getHasStack() && energyOutput.isItemValid(itemStack)) {
                        energyOutput.putStack(itemStack1);
                        slot.putStack(itemStack);
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(itemStack, 36, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
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
