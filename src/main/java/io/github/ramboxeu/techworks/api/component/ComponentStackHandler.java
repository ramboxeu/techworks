package io.github.ramboxeu.techworks.api.component;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ComponentStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
    private final NonNullList<ItemStack> stacks;
    private final ImmutableList<ItemStack> defaults;


    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    private ComponentStackHandler(ImmutableList<ItemStack> defaults) {
        stacks = NonNullList.from(ItemStack.EMPTY, defaults.toArray(new ItemStack[defaults.size()]));
        this.defaults = defaults;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlot(slot);
        validateStack(slot, stack);
        stacks.set(slot, stack);
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot >= stacks.size() || !isItemValid(slot, stack)) {
            return stack;
        }

        // We have a hardcoded limit of 1, so we don't have to worry about merging and things
        ItemStack existing = stacks.get(slot);

        if (!existing.isEmpty() && !existing.isItemEqual(defaults.get(slot))) {
            return stack;
        }

        ItemStack entry = stack.split(1);

        if (!simulate) {
            stacks.set(slot, entry);
        }

        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0 || slot < 0 || slot >= stacks.size()) {
            return ItemStack.EMPTY;
        }

        // We should be able to guarantee that this stacks all have size of 1
        ItemStack existing = stacks.get(slot);

        if (existing.isEmpty() || existing.isItemEqual(defaults.get(slot))) {
            return ItemStack.EMPTY;
        }

        if (!simulate) {
            stacks.set(slot, defaults.get(slot));
        }

        return existing.copy();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof ComponentItem;
    }

    @Override
    public CompoundNBT serializeNBT() {
        ListNBT listTag = new ListNBT();

        for (int i = 0; i < stacks.size(); i++) {
            if(!stacks.get(i).isEmpty()) {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("Slot", i);
                stacks.get(i).write(itemTag);
                listTag.add(itemTag);
            }
        }

        CompoundNBT invTag = new CompoundNBT();
        invTag.put("Items", listTag);
//        invTag.putInt("Size", );
        return invTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT listTag = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundNBT itemTag = listTag.getCompound(i);
            int slot = itemTag.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(i, ItemStack.read(itemTag));
            }
        }
    }

    public Stream<ItemStack> stream() {
        return stacks.stream();
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= stacks.size()) {
            throw new IllegalArgumentException(slot + " is outside of rage [0," + stacks.size() + ")");
        }
    }

    private void validateStack(int slot, ItemStack stack) {
        if (!isItemValid(slot, stack)) {
            throw new IllegalArgumentException(stack.getItem() + " is not a valid item (slot: " + slot + ")");
        }
    }

    protected void onContentsChanged(int slot) {

    }

    public static class Builder {
        private final List<ItemStack> defaults = new ArrayList<>();

        public Builder defaults(ComponentItem... items) {
            for (ComponentItem item : items) {
                defaults.add(new ItemStack(item));
            }

            return this;
        }

        public ComponentStackHandler build() {
            return new ComponentStackHandler(ImmutableList.copyOf(defaults));
        }
    }
}
