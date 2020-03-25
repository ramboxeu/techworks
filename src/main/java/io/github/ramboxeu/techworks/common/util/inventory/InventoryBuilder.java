package io.github.ramboxeu.techworks.common.util.inventory;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

// TODO: Implement
public class InventoryBuilder {
    private final IItemHandler inventory;
    private final List<SlotItemHandler> slots = new ArrayList<>();
    private int slotIndex;

    public InventoryBuilder(IItemHandler inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder addSlot(int x, int y, SlotType type) {
        return this;
    }

    public enum SlotType {
        NORMAL,
        OUTPUT,
        INPUT
    }
}
