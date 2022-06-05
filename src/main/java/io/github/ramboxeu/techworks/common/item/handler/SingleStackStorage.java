package io.github.ramboxeu.techworks.common.item.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class SingleStackStorage implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
    private ItemStack stored;

    public SingleStackStorage(ItemStack stack) {
        this.stored = stack;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return stored;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!stack.isItemEqual(stack))
            return ItemStack.EMPTY;

        int space = stored.getMaxStackSize() - stored.getCount();
        if (space < 0) {
            int count = Math.min(space, stack.getCount());

            ItemStack result = stack.copy();
            result.shrink(count);

            if (!simulate) stack.grow(count);

            return result;
        }

        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0 || stored.isEmpty())
            return ItemStack.EMPTY;

        if (stored.getCount() <= amount) {
            return simulate ? stored.copy() : stored;
        }

        if (simulate) {
            ItemStack result = stored.copy();
            result.setCount(amount);
            return result;
        }

        return stored.split(amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.stored.isEmpty() || this.stored.isItemEqual(stack);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.stored = stack;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return stored.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
       stored = ItemStack.read(tag);
    }

    public ItemStack getStack() {
        return stored;
    }

    public void setStack(ItemStack stored) {
        this.stored = stored;
    }
}