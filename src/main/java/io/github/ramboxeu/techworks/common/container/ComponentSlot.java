package io.github.ramboxeu.techworks.common.container;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ComponentSlot extends Slot {
    private final ComponentInventory componentInventory;

    public ComponentSlot(ComponentInventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
        componentInventory = inventory;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
//        return componentInventory.isValidInvStack(this.id, stack);
        return false;
    }
}
