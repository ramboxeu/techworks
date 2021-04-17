package io.github.ramboxeu.techworks.common.item.handler;

import net.minecraftforge.items.IItemHandler;

public class ItemRange {
    private final int minSlot;
    private final int maxSlot;
    private final IItemHandler handler;

    public ItemRange(int minSlot, int maxSlot, IItemHandler handler) {
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
        this.handler = handler;
    }

    public static ItemRange of(IItemHandler handler) {
        return of(handler, 0, handler.getSlots() - 1);
    }

    public static ItemRange of(IItemHandler handler, int minSlot, int maxSlot) {
        if (maxSlot < minSlot) {
            throw new IllegalArgumentException("Max slot must be grater than min slot");
        }

        return new ItemRange(minSlot, maxSlot, handler);
    }

    public int getMinSlot() {
        return minSlot;
    }

    public int getMaxSlot() {
        return maxSlot;
    }

    public IItemHandler getHandler() {
        return handler;
    }
}
