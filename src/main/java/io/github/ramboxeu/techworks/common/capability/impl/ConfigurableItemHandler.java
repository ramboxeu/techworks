package io.github.ramboxeu.techworks.common.capability.impl;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class ConfigurableItemHandler implements IItemHandlerModifiable {
    private MachinePort.Mode mode;
    private IItemHandlerModifiable parent;

    public ConfigurableItemHandler(MachinePort.Mode mode, IItemHandlerModifiable parent) {
        this.mode = mode;
        this.parent = parent;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        // Should this work according to mode or not?
        if (mode.canInput()) {
            parent.setStackInSlot(slot, stack);
        } else {
            Techworks.LOGGER.warn("Attempted to set stack when output mode");
        }
    }

    @Override
    public int getSlots() {
        return parent.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        // Should this work according to mode or not?
        if (mode.canOutput()) {
            return parent.getStackInSlot(slot);
        } else {
            Techworks.LOGGER.warn("Attempted to get stack when input mode");
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return mode.canInput() ? parent.insertItem(slot, stack, simulate) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return mode.canOutput() ? parent.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return parent.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return parent.isItemValid(slot, stack);
    }

    public MachinePort.Mode getMode() {
        return mode;
    }
}
