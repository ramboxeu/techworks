package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import io.github.ramboxeu.techworks.common.tile.BoilerTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.inventory.InventoryBuilder;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class BoilerContainer extends AbstractMachineContainer {

    public BoilerContainer(int id, PlayerInventory playerInventory, AbstractMachineTile tile) {
        super(Registration.BOILER_CONTAINER.get(), id, playerInventory, tile);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    protected void layoutSlots(InventoryBuilder builder) {
        builder
            .addSlot(new SlotBuilder(80, 54).predicate(PredicateUtils::isFuel))
            .addSlot(new SlotBuilder(27, 16).limit(1).predicate(PredicateUtils::isFluidHandler).onChanged((stack, slot) -> {
                ((BoilerTile) machineTile).handleFluidHandlerInput(stack, slot);
                this.detectAndSendChanges();
            }))
            .addSlot(new SlotBuilder(27, 54).limit(1).predicate(PredicateUtils::isFluidHandler).onChanged((slot, stack) -> {
                ((BoilerTile) machineTile).handleFluidHandlerOutput(slot, stack);
                this.detectAndSendChanges();
            }))
            .addSlot(new SlotBuilder(133, 16).limit(1).predicate(itemStack -> false))
            .addSlot(new SlotBuilder(133, 54).limit(1).predicate(itemStack -> false));
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
                if (ForgeHooks.getBurnTime(itemStack) > 0) {
                    if (!this.mergeItemStack(itemStack, 36, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
                    Slot fluidInputSlot = this.inventorySlots.get(37);
                    Slot fluidOutputSlot = this.inventorySlots.get(38);

                    ItemStack itemStack1 = ItemStack.EMPTY;

                    itemStack1 = itemStack.split(1);

                    if (!fluidInputSlot.getHasStack() && fluidInputSlot.isItemValid(itemStack)) {
                        fluidInputSlot.putStack(itemStack1);
                        slot.putStack(itemStack);
                        return ItemStack.EMPTY;
                    }

                    if (!fluidOutputSlot.getHasStack() && fluidOutputSlot.isItemValid(itemStack)) {
                        fluidOutputSlot.putStack(itemStack1);
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

        return itemStack;
    }

}
