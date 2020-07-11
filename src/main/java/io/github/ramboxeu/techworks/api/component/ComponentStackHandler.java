package io.github.ramboxeu.techworks.api.component;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ComponentStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
    private NonNullList<ItemStack> stacks;
    private ImmutableList<ItemStack> defaults;
    private NonNullList<Slot> slots;

    public static ComponentStackHandler withSize(int size) {
        return new ComponentStackHandler(NonNullList.withSize(size, ItemStack.EMPTY), NonNullList.withSize(size, new Slot()));
    }

    public static ComponentStackHandler empty() {
        return new ComponentStackHandler(NonNullList.create(), NonNullList.create());
    }

    public static ComponentStackHandler withBuilder(Builder builder) {
        return new ComponentStackHandler(builder.stacks, builder.slots);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Deprecated
    private ComponentStackHandler(ImmutableList<ItemStack> defaults) {
        stacks = NonNullList.from(ItemStack.EMPTY, defaults.toArray(new ItemStack[defaults.size()]));
        this.defaults = defaults;
    }

    private ComponentStackHandler(NonNullList<ItemStack> stacks, NonNullList<Slot> slots) {
        this.stacks = stacks;
        this.slots = slots;
//        this.defaults = defaults;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlot(slot);
        validateStack(slot, stack);
        stacks.set(slot, stack);
        onContentsChanged(slot);
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

        if (!existing.isEmpty() /*&& !existing.isItemEqual(defaults.get(slot))*/) {
            return stack;
        }

        ItemStack entry = stack.split(1);

        if (!simulate) {
            stacks.set(slot, entry);
            onContentsChanged(slot);
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

        if (existing.isEmpty() /*|| existing.isItemEqual(defaults.get(slot))*/) {
            return ItemStack.EMPTY;
        }

        if (!simulate) {
            stacks.set(slot, ItemStack.EMPTY);
            onContentsChanged(slot);
        }

        return existing.copy();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return (stack.getItem() instanceof ComponentItem && slots.get(slot).predicate.test(stack)) || stack.isEmpty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        ListNBT itemsTag = new ListNBT();
//        ListNBT defaultsTag = new ListNBT();

        for (int i = 0; i < stacks.size(); i++) {
            if(!stacks.get(i).isEmpty()) {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("Slot", i);
                stacks.get(i).write(itemTag);
                itemsTag.add(itemTag);
            }
        }

//        for (int i = 0; i < defaults.size(); i++) {
//            CompoundNBT itemTag = new CompoundNBT();
//            itemTag.putInt("Slot", i);
//            stacks.get(i).write(itemTag);
//            defaultsTag.add(itemTag);
//        }

        CompoundNBT invTag = new CompoundNBT();
        invTag.put("Items", itemsTag);
//        invTag.put("Defaults", defaultsTag);
        invTag.putInt("Size", stacks.size());
        return invTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT itemsTag = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        if (nbt.contains("Size", Constants.NBT.TAG_INT)) {
            this.stacks = NonNullList.withSize(nbt.getInt("Size"), ItemStack.EMPTY);
        }
//        ListNBT defaultsTag = nbt.getList("Defaults", Constants.NBT.TAG_COMPOUND);
//        int size = nbt.getInt("Size");
//
//        List<ItemStack> tempDefaults = new ArrayList<>();
//
//        for (int i = 0; i < size; i++) {
//            CompoundNBT itemTag = defaultsTag.getCompound(i);
//            int slot = itemTag.getInt("Slot");
//
//            tempDefaults.add(slot, ItemStack.read(itemTag));
//        }

//        ImmutableList<ItemStack> defaults = ImmutableList.copyOf(tempDefaults);
//        NonNullList<ItemStack> stacks = NonNullList.from(ItemStack.EMPTY, );

        for (int i = 0; i < itemsTag.size(); i++) {
            CompoundNBT itemTag = itemsTag.getCompound(i);
            int slot = itemTag.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(i, ItemStack.read(itemTag));
            }
        }

//        this.stacks = stacks;
//        this.defaults = defaults;
    }

    public Stream<ItemStack> stream() {
        return stacks.stream();
    }

    public NonNullList<Slot> getSlotList() {
        return slots;
    }

    //    public ImmutableList<ItemStack> getDefaults() {
//        return defaults;
//    }

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
        private final int size;
        private final NonNullList<Slot> slots;
        private NonNullList<ItemStack> stacks;

        public Builder(int size) {
            this.size = size;
            slots = NonNullList.withSize(size, new Slot());
            stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        }

        public Builder slot(int index, Slot slot) {
            slots.set(index, slot);
            return this;
        }

        public Builder copyStacks(NonNullList<ItemStack> stacks) {
            this.stacks = stacks;
            return this;
        }
//        private final List<ItemStack> defaults = new ArrayList<>();
//
//        public Builder defaults(ComponentItem... items) {
//            for (ComponentItem item : items) {
//                defaults.add(new ItemStack(item));
//            }
//
//            return this;
//        }
//
//        public ComponentStackHandler build() {
//            return new ComponentStackHandler(ImmutableList.copyOf(defaults));
//        }
//
//        public ComponentStackHandler empty() {
//            return new ComponentStackHandler(ImmutableList.of());
//        }
    }

    public static class Slot {
        private final Predicate<ItemStack> predicate;
        private final ResourceLocation texture;

        public Slot(Predicate<ItemStack> predicate, ResourceLocation texture) {
            this.predicate = predicate;
            this.texture = texture;
        }

        public Slot() {
            this(stack -> true, null);
        }

        public Optional<ResourceLocation> getTexture() {
            if (texture == null) {
                return Optional.empty();
            } else {
                return Optional.of(texture);
            }
        }
    }
}
