package io.github.ramboxeu.techworks.common.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemUtils {

    public static final IInventory EMPTY_INV = new Inventory(0);

    public static ItemStack fromFirstNotEmpty(IItemHandler handler, int maxAmount) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slotStack = handler.getStackInSlot(i);

            if (!slotStack.isEmpty())
                return handler.extractItem(i, maxAmount, false);
        }

        return ItemStack.EMPTY;
    }

    public static List<ItemStack> concatItemHandlers(IItemHandler ...handlers) {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (IItemHandler handler : handlers) {
            for (int i = 0; i < handler.getSlots(); i++) {
                stacks.add(handler.getStackInSlot(i));
            }
        }

        return stacks;
    }

    public static boolean isHandlerFull(ItemStackHandler handler) {
        for (int i = 0, size = handler.getSlots(); i < size; i++) {
            if (handler.getStackInSlot(i).getCount() < handler.getSlotLimit(i))
                return false;
        }

        return true;
    }

    public static boolean canInsertItems(IItemHandler handler, Collection<ItemStack> stacks) {
        return insertItems(handler, stacks, true).isEmpty();
    }

    public static ItemStack insertItem(IItemHandler handler, ItemStack stack, boolean simulate) {
        ItemStack remainder = stack;

        for (int i = 0, size = handler.getSlots(); i < size; i++) {
            ItemStack input = remainder;
            remainder = handler.insertItem(i, input, simulate);

            if (remainder.isEmpty())
                return ItemStack.EMPTY;
        }

        return remainder;
    }

    public static List<ItemStack> insertItems(IItemHandler handler, Collection<ItemStack> stacks, boolean simulate) {
        List<ItemStack> remainders = null;

        for (ItemStack stack : stacks) {
            ItemStack remainder = insertItem(handler, stack, simulate);

            if (!remainder.isEmpty()) {
                if (remainders == null)
                    remainders = new ArrayList<>(stacks.size());

                remainders.add(remainder);
            }
        }

        return remainders == null ? Collections.emptyList() : remainders;
    }
}
