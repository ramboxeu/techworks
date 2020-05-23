package io.github.ramboxeu.techworks.common.capability;

import io.github.ramboxeu.techworks.common.capability.extensions.IInventoryItemStackHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

// When Forge and Vanilla have their own idea about things
public class InventoryItemStackHandler implements IInventoryItemStackHandler {
    protected NonNullList<ItemStack> stacks;

    public InventoryItemStackHandler(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public InventoryItemStackHandler(ItemStack... stacks) {
        this.stacks = NonNullList.from(ItemStack.EMPTY, stacks);
    }

    protected void onContentsChanged(int slot) {}

    // IItemHandler start
    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return stacks.size() > index ? stacks.get(index) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (slot > stacks.size()) {
            return stack;
        }

        if (!isItemValid(slot, stack)) {
            return stack;
        }

        ItemStack existing = stacks.get(slot);

        int limit = getSlotLimit(slot);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(existing, stack)) {
                return stack;
            }

            limit -= existing.getCount();
        }

        if (limit <= 0) {
            return stack;
        }

        boolean isLimitReached = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                stacks.set(slot, isLimitReached ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(isLimitReached ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return isLimitReached ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }

        if (slot > stacks.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = stacks.get(slot);

        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int extract = Math.min(amount, existing.getCount());

        if (!simulate) {
            stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - extract));
            onContentsChanged(slot);
        }

        return ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - extract);
    }

    @Override
    public int getSlotLimit(int slot) {
        return stacks.size() > slot ? stacks.get(slot).getMaxStackSize() : 0;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }
    // IItemHandler end

    // IItemHandlerModifiable
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stacks.size() > slot) {
            stacks.set(slot, stack);
        }
    }

    // IInventory start
    @Override
    public int getSizeInventory() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return extractItem(index, count, false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return stacks.size() > index ? stacks.remove(index) : ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        setStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }


    @Override
    public void clear() {
        stacks.clear();
    }
    // IInventory end
}
