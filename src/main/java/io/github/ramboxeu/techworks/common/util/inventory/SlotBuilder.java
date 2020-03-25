package io.github.ramboxeu.techworks.common.util.inventory;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SlotBuilder {
    private int x;
    private int y;
    private Predicate<ItemStack> predicate;
    private boolean isOutput;
    private int stackLimit;

    public SlotBuilder(int x, int y) {
        this.x = x;
        this.y = y;
        this.predicate = itemStack -> true;
        this.stackLimit = 64;
    }

    public SlotBuilder() {
        this(0,0);
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
        Techworks.LOGGER.info("Set stack limit to: {}", stackLimit);
        return this;
    }

    public SlotItemHandler build(IItemHandler itemHandler, int index) {
        return new SlotItemHandler(itemHandler, index, this.x, this.y) {
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
        };
    }
}
