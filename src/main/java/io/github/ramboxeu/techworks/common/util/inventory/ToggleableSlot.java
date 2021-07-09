package io.github.ramboxeu.techworks.common.util.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ToggleableSlot extends SlotItemHandler {

    private boolean enabled = true;

    public ToggleableSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return enabled;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return enabled && super.isItemValid(stack);
    }

    @Nonnull
    @Override
    public ItemStack getStack() {
        return enabled ? super.getStack() : ItemStack.EMPTY;
    }
}
