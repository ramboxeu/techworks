package io.github.ramboxeu.techworks.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

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

}
