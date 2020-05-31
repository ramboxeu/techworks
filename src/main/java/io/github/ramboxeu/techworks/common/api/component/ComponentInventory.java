package io.github.ramboxeu.techworks.common.api.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

/**
 * Stores both ItemStacks of IComponent Items and IComponents itself
 * @param <T> type of container this is attached to
 */
public class ComponentInventory<T> implements IComponentList<T>, Inventory {
    private T container;
    private DefaultedList<Entry> entries;

    public ComponentInventory(T container, int capacity) {
        this.container = container;
        this.entries = DefaultedList.ofSize(capacity, Entry.EMPTY);
    }

    // IComponentList

    @Override
    public boolean add(IComponent component) {
        if (containsType(component.getType())) {
            return false;
        }

        return entries.add(new Entry(component));
    }

    @Override
    public boolean remove(IComponent component) {
        if (!containsType(component.getType())) {
            return false;
        }

        int index = getIndexOfComponent(component);

        if (index > 0) {
            entries.remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T getContainer() {
        return container;
    }

    @Override
    public IComponent getComponent(ComponentType type) {
        return getComponentByType(type);
    }

    @Override
    public boolean hasComponent(ComponentType type) {
        return hasComponent(type);
    }

    @Override
    public void tick() {
        entries.forEach(entry -> entry.component.tick());
    }

    // IInventory

    @Override
    public int getInvSize() {
        return entries.size();
    }

    @Override
    public boolean isInvEmpty() {
        return entries.isEmpty();
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return slot > 0 && slot < entries.size() ? entries.get(slot).itemStack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        if (slot > entries.size() || slot < 0 || entries.get(slot).itemStack.isEmpty() || amount < 0) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = entries.get(slot).itemStack.split(amount);

        if (itemStack.isEmpty()) {
            entries.set(slot, Entry.EMPTY);
        } else {
            this.markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        if (slot < 0 || slot > entries.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = entries.get(slot).itemStack;

        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            entries.set(slot, Entry.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        entries.set(slot, new Entry(stack));
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {
        entries.clear();
    }

    // Utils
    private boolean containsType(ComponentType type) {
        for (Entry entry : entries) {
            if (entry.type == type) {
                return true;
            }
        }

        return false;
    }

    private int getIndexOfComponent(IComponent component) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).component == component) {
                return i;
            }
        }

        return -1;
    }

    private IComponent getComponentByType(ComponentType type) {
        for (Entry entry : entries) {
            if (entry.type == type) {
                return entry.component;
            }
        }

        return null;
    }

    private static class Entry {
        private ItemStack itemStack;
        private final ComponentType type;
        private final IComponent component;

        private static Entry EMPTY = new Entry(null, null, null);

        public Entry(ItemStack itemStack, ComponentType type, IComponent component) {
            this.itemStack = itemStack;
            this.type = type;
            this.component = component;
        }

        public Entry(IComponent component) {
            this.itemStack = ItemStack.EMPTY;
            this.type = component.getType();
            this.component = component;
        }

        public Entry(ItemStack stack) {
            this.itemStack = stack;
            if (itemStack.getItem() instanceof IComponent) {
                IComponent c = (IComponent) itemStack.getItem();
                this.type = c.getType();
                this.component = c;
            } else {
                this.type = null;
                this.component = null;
            }
        }
    }
}