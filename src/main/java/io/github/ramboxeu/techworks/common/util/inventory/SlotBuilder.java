package io.github.ramboxeu.techworks.common.util.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class SlotBuilder {
    private int x;
    private int y;
    private Predicate<ItemStack> predicate;
    private boolean isOutput;
    private int stackLimit;
    private BiConsumer<ItemStack, SlotItemHandler> onChangedListener;
    private final IItemHandler inv;
    private final int index;

    public SlotBuilder(IItemHandler inv, int index) {
        this.x = 0;
        this.y = 0;
        this.predicate = itemStack -> inv.isItemValid(index, itemStack);
        this.stackLimit = 64;
        this.onChangedListener = ((itemStack, slotItemHandler) -> {});
        this.inv = inv;
        this.index = index;
    }

    public SlotBuilder pos(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public SlotBuilder output(boolean output) {
        this.isOutput = output;
        return this;
    }

    public SlotBuilder predicate(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
        return this;
    }

    public SlotBuilder limit(int stackLimit) {
        this.stackLimit = stackLimit;
        return this;
    }

    public SlotBuilder onChanged(BiConsumer<ItemStack, SlotItemHandler> listener) {
        this.onChangedListener = listener;
        return this;
    }

    public SlotItemHandler build() {
        return new SlotItemHandler(inv, index, x, y) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return isOutput || predicate.test(stack);
            }

            @Override
            public int getSlotStackLimit() {
                return stackLimit;
            }

            public int getItemStackLimit(ItemStack stack) {
                return stackLimit;
            }

            public void onSlotChanged() {
                onChangedListener.accept(this.getStack(), this);
                super.onSlotChanged();
            }
        };
    }
}
