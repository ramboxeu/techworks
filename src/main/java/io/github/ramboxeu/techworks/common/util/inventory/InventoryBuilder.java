package io.github.ramboxeu.techworks.common.util.inventory;

import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class InventoryBuilder {
    private final List<SlotBuilder> slots = new ArrayList<>();

    public InventoryBuilder() {}

    public InventoryBuilder addSlot(SlotBuilder builder) {
        slots.add(builder);
        return this;
    }

    public SlotItemHandler[] build(IItemHandler itemHandler) {
        ArrayList<SlotItemHandler> slotItemHandlers = new ArrayList<>();

        for (int i = 0; i < slots.size(); i++) {
            SlotBuilder builder = slots.get(i);
            slotItemHandlers.add(builder.build(itemHandler, i));
        }

        return slotItemHandlers.toArray(new SlotItemHandler[] {});
    }
}
