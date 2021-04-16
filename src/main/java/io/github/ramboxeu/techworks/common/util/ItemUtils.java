package io.github.ramboxeu.techworks.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemUtils {

    public static ItemStack fromFirstNotEmpty(IItemHandler handler, int maxAmount) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slotStack = handler.getStackInSlot(i);

            if (!slotStack.isEmpty())
                return handler.extractItem(i, maxAmount, false);
        }

        return ItemStack.EMPTY;
    }
}
