package io.github.ramboxeu.techworks.common.capability.impl;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DevItemHandler implements IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
    private final DevBlockTile tile;
    private List<ItemStack> stacks = new ArrayList<>();

    public DevItemHandler(DevBlockTile tile) {
        this.tile = tile;
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= stacks.size()) {
            return ItemStack.EMPTY;
        }

        return stacks.get(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        tile.createLog("I R " + (simulate ? "sim " : "exe ") + stack + " " + slot);
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= stacks.size()) {
            tile.createLog("I E " + (simulate ? "sim " : "exe ") + amount + " EMPTY " + slot + "(" + (stacks.size() - 1) + ")");
            return ItemStack.EMPTY;
        }

        ItemStack stack = stacks.get(slot);
        tile.createLog("I E " + (simulate ? "sim " : "exe ") + stack + " " + slot);
        ItemStack copy = stack.copy();
        copy.setCount(Math.min(stack.getCount(), amount));
        return copy;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        tile.createLog("I S " + stack + " " + slot);
    }

    public void setStacks(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        ListNBT list = new ListNBT();

        for (int i = 0; i < stacks.size(); i++) {
            list.add(i, stacks.get(i).serializeNBT());
        }

        tag.put("Stacks", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("Stacks", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> stacks = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            stacks.add(i, ItemStack.read(list.getCompound(i)));
        }

        this.stacks = stacks;
    }
}
