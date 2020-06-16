package io.github.ramboxeu.techworks.common.api.component;

import io.github.ramboxeu.techworks.common.registry.TechworksRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Stores both ItemStacks of IComponent Items and IComponents itself. This should keep itself in sync.
 * @param <T> type of container this is attached to
 */
public class ComponentInventory<T> implements Inventory, IComponentList<T> {
    private T container;
    private DefaultedList<ItemStack> itemStacks;
    private List<IComponent> components;
    private Slot[] slots;

    public ComponentInventory(T container, Slot ...slots) {
        this.container = container;
        this.itemStacks = DefaultedList.ofSize(slots.length, ItemStack.EMPTY);
        this.components = DefaultedList.ofSize(slots.length, EmptyComponent.INSTANCE);
        this.slots = slots;
    }

    // IInventory

    @Override
    public int getInvSize() {
        return itemStacks.size();
    }

    @Override
    public boolean isInvEmpty() {
        return itemStacks.isEmpty();
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return slot >= 0 && slot < itemStacks.size() ? itemStacks.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        if (slot >= itemStacks.size() || slot < 0 || itemStacks.get(slot).isEmpty() || amount < 0) {
            return ItemStack.EMPTY;
        }

        ItemStack existingStack = itemStacks.get(slot);
        ItemStack newStack = existingStack.split(amount);

        if (existingStack.isEmpty()) {
            itemStacks.set(slot, ItemStack.EMPTY);
            components.set(slot, EmptyComponent.INSTANCE);
        }
        onContentsChanged();

        return newStack;
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        if (slot < 0 || slot >= itemStacks.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = itemStacks.get(slot);

        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            itemStacks.set(slot, ItemStack.EMPTY);
            components.set(slot, EmptyComponent.INSTANCE);
            onContentsChanged();
            return itemStack;
        }
    }

    // This does void items that aren't providers and mismatched types
    // Empty stacks will work
    @Override
    public void setInvStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            itemStacks.set(slot, stack);
            components.set(slot, EmptyComponent.INSTANCE);
        }

        if (stack.getItem() instanceof IComponentProvider) {
            IComponentProvider provider = (IComponentProvider)stack.getItem();
            IComponent component = provider.create(this);
            if (component.getType().equals(slots[slot].type)) {
                itemStacks.set(slot, stack);
                components.set(slot, provider.create(this));
                onContentsChanged();
            }
        }
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return false;
    }

    protected void onContentsChanged(){}

    @Override
    public void clear() {
        itemStacks.clear();
        components.clear();
    }

    @Override
    public boolean isValidInvStack(int index, ItemStack stack) {
        if (index < 0 || index >= itemStacks.size()) {
            return false;
        }

        if (stack.getItem() instanceof IComponentProvider) {
            Slot slot = slots[index];
            IComponent component = ((IComponentProvider) stack.getItem()).create(this);

            if (slot.hasPredicate) {
                if(slot.predicate.test(stack, component)) {
                    return true;
                } else {
                    return component.getType().equals(slot.type);
                }
            } else {
                return component.getType().equals(slot.type);
            }
        }

        return false;
    }

    //    // Utils
//    private boolean containsType(ComponentType type) {
//        return this.components.stream().anyMatch(component -> component.getType().equals(type));
//    }
//
//    private int getIndexOfComponent(IComponent component) {
//        for (int i = 0; i < entries.size(); i++) {
//            if (entries.get(i).component == component) {
//                return i;
//            }
//        }
//
//        return -1;
//    }
//
//    private IComponent getComponentByType(ComponentType type) {
//        for (Entry entry : entries) {
//            if (entry.type == type) {
//                return entry.component;
//            }
//        }
//
//        return null;
//    }
//
//    private int firstEmptySlot() {
//        for (int i = 0; i < this.components.size(); i++) {
//            if (components.get(i).equals(EmptyComponent.INSTANCE)) {
//                return i;
//            }
//        }
//
//        return -1;
//    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        ListTag itemsTag = new ListTag();
        ListTag componentsTag = new ListTag();

        for (int i = 0; i < itemStacks.size(); ++i) {
            ItemStack itemStack = itemStacks.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                itemStack.toTag(itemTag);
                itemsTag.add(itemTag);
            }
        }

        for (int i = 0; i < components.size(); ++i) {
            IComponent component = components.get(i);
            if (!component.equals(EmptyComponent.INSTANCE)) {
                CompoundTag componentTag = new CompoundTag();
                componentTag.putString("Provider", TechworksRegistries.COMPONENT_PROVIDER.getId(component.getProvider()).toString());
                componentTag.putByte("Index", (byte) i);
                component.toTag(componentTag);
                componentsTag.add(componentTag);
            }
        }

        tag.put("ItemStacks", itemsTag);
        tag.put("Components", componentsTag);

        return tag;
    }

    public void fromTag(CompoundTag tag) {
        ListTag itemsListTag = tag.getList("ItemStacks", 10);
        ListTag componentsListTag = tag.getList("Components", 10);

        for (int i = 0; i < itemsListTag.size(); ++i) {
            CompoundTag itemTag = itemsListTag.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot >= 0 && slot < itemStacks.size()) {
                itemStacks.set(slot, ItemStack.fromTag(itemTag));
            }
        }

        for (int i = 0; i < componentsListTag.size(); i++) {
            CompoundTag componentTag = componentsListTag.getCompound(i);
            int index = componentTag.getByte("Index") & 255;
            Identifier providerIdentifier = new Identifier(componentTag.getString("Provider"));
            IComponentProvider provider = TechworksRegistries.COMPONENT_PROVIDER.get(providerIdentifier);
            IComponent component = provider.create(this);
            component.fromTag(componentTag);
            components.set(index, component);
        }
    }

    @Override
    public T getContainer() {
        return container;
    }

    @Override
    public Stream<IComponent> stream() {
        return components.stream();
    }

    @Override
    public Optional<IComponent> find(Predicate<IComponent> predicate) {
        return components.stream().filter(predicate).findFirst();
    }

    public void tick() {
        components.forEach(IComponent::tick);
    }

    public static class Slot {
        private ComponentType type;
        private BiPredicate<ItemStack, IComponent> predicate;
        private final boolean hasPredicate;

        public Slot(ComponentType type, BiPredicate<ItemStack, IComponent> predicate) {
            this.type = type;
            this.predicate = predicate;
            this.hasPredicate = true;
        }

        public Slot(BiPredicate<ItemStack, IComponent> predicate) {
            this.predicate = predicate;
            this.hasPredicate = true;
        }

        public Slot(ComponentType type) {
            this.type = type;
            this.hasPredicate = false;
        }

        public Slot() {
            this.predicate = (itemStack, component) -> true;
            this.hasPredicate = true;
        }

//        public ComponentType getType() {
//            return type;
//        }
//
//        public BiPredicate<ItemStack, IComponent> getPredicate() {
//            return predicate;
//        }
//
//        public boolean hasPredicate() {
//            return hasPredicate;
//        }
    }
}
