package io.github.ramboxeu.techworks.common.component;

import com.google.common.collect.ImmutableMap;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class ComponentStorage implements IItemHandler, INBTSerializable<CompoundNBT>, Iterable<ComponentStorage.Entry> {

    private final Map<ComponentType<?>, Entry> storage;
    private ComponentType<?> pendingComponent;
    private ItemStack pendingStack = ItemStack.EMPTY;
    private Operation operation = Operation.NONE;
    private int waitTimer = 0;
    private boolean finished = false;

    private ComponentStorage(Map<ComponentType<?>, Entry> storage) {
        this.storage = storage;

        storage.forEach((t, e) -> e.init());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt("WaitTimer", waitTimer);
        tag.putBoolean("Finished", finished);
        tag.put("PendingStack", pendingStack.serializeNBT());
        tag.put("Storage", serializeStorage());
        NBTUtils.serializeEnum(tag, "Operation", operation);

        if (pendingComponent != null)
            NBTUtils.serializeComponentType(tag, "PendingComponent", pendingComponent);

        return tag;
    }

    private ListNBT serializeStorage() {
        ListNBT componentsTag = new ListNBT();

        int i = 0;
        for (Entry entry : storage.values()) {
            CompoundNBT componentTag = new CompoundNBT();
            NBTUtils.serializeComponent(componentTag, "Component", entry.getComponent());
            componentTag.put("Item", entry.getItemStack().write(new CompoundNBT()));
            componentsTag.add(i++, componentTag);
        }

        return componentsTag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        waitTimer = tag.getInt("WaitTimer");
        finished = tag.getBoolean("Finished");
        pendingStack = ItemStack.read(tag.getCompound("PendingStack"));
        deserializeStorage(tag.getList("Storage", Constants.NBT.TAG_COMPOUND));

        operation = NBTUtils.deserializeEnum(tag, "Operation", Operation.class);

        if (operation == null)
            operation = Operation.NONE;

        pendingComponent = NBTUtils.deserializeComponentType(tag, "PendingComponent");
    }

    private void deserializeStorage(ListNBT componentsTag) {
        for (INBT tag : componentsTag) {
            CompoundNBT componentTag = (CompoundNBT) tag;
            Component component = NBTUtils.deserializeComponent(componentTag, "Component");
            ItemStack stack = ItemStack.read(componentTag.getCompound("Item"));
            storage.get(component.getType()).update(component, stack);
        }
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return pendingStack;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            if (!simulate) {
                clear();
                return ItemStack.EMPTY;
            }

            return stack.copy();
        }

        if (canInstall(stack)) {
            if (!simulate) {
                waitTimer = 0;
                pendingStack = stack;
                operation = Operation.INSTALL;
                finished = false;

                if (stack.getCount() > 1)
                    return pendingStack.split(1);

                return ItemStack.EMPTY;
            }

            ItemStack ret = stack.copy();
            ret.shrink(1);

            return ret;
        }

        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (operation == Operation.INSTALL) {
            if (!simulate) {
                ItemStack stack = pendingStack;
                clear();
                return stack;
            }

            return pendingStack.copy();
        }

        if (operation == Operation.UNINSTALL) {
            return ItemStack.EMPTY;
        }

        if (finished) {
            if (!simulate) {
                ItemStack stack = pendingStack;
                clear();
                return stack;
            }

            return pendingStack.copy();
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return ComponentManager.getInstance().isItemComponent(stack.getItem());
    }

    @Nonnull
    @Override
    public Iterator<Entry> iterator() {
        return storage.values().iterator();
    }

    public void tick() {
        if (!finished) {
            if (waitTimer == 200) {
                finished = true;
                waitTimer = 0;

                if (operation == Operation.INSTALL)
                    insert();

                if (operation == Operation.UNINSTALL)
                    extract();

                operation = Operation.NONE;
            } else {
                if (operation.canTick()) {
                    waitTimer++;
                }
            }
        }
    }

    public boolean uninstall(ComponentType<?> type) {
        if (canUninstall()) {
            Entry entry = storage.get(type);

            if (entry != null) {
                if (entry.isBase())
                    return false;

                if (operation == Operation.UNINSTALL)
                    waitTimer = 0;

                finished = false;
                pendingComponent = type;
                pendingStack = entry.stack;
                operation = Operation.UNINSTALL;

                return true;
            }

            return false;
        }

        return false;
    }

    public List<Component> getComponents() {
        return storage.values().stream().map(e -> e.component).collect(Collectors.toList());
    }

    public Collection<ComponentType<?>> getSupportedTypes() {
        return storage.keySet();
    }

    public boolean isFinished() {
        return finished;
    }

    private void clear() {
        pendingStack = ItemStack.EMPTY;
        waitTimer = 0;
        operation = Operation.NONE;
        finished = false;
    }

    private boolean canInstall(ItemStack stack) {
        Component component = ComponentManager.getInstance().getComponent(stack.getItem());

        if (component != null) {
            return storage.values().stream().noneMatch(entry -> entry.match(component, stack))
                    && storage.get(component.getType()).isBase()
                    && operation.canProcess()
                    && pendingStack.isEmpty()
                    && !finished;
        }

        return false;
    }

    private boolean canUninstall() {
        if (operation.canProcessOtherType()) {
            if (!pendingStack.isEmpty())
                return pendingComponent != null;

            return true;
        }

        return false;
    }

    private void insert() {
        Component component = ComponentManager.getInstance().getComponent(pendingStack.getItem());

        if (component != null) {
            Entry entry = storage.get(component.getType());
            entry.update(component, pendingStack);
        }

        pendingStack = ItemStack.EMPTY;
        finished = false;
    }

    private void extract() {
        Entry entry = storage.get(pendingComponent);
        entry.update(ComponentManager.getInstance().getComponent(pendingComponent.getBaseComponentId()), null);

        pendingComponent = null;
    }

    public float getOperationProgress() {
        return waitTimer / 200.0f;
    }

    public boolean isProcessing() {
        return operation != Operation.NONE;
    }

    public Operation getOperation() {
        return operation;
    }

    public void onItemStackChange() {
        if (pendingStack.isEmpty()) {
            clear();
        }
    }

    public enum Operation {
        NONE,
        INSTALL,
        UNINSTALL;

        private boolean canProcess() {
            return this == NONE;
        }

        public boolean canProcessOtherType() {
            return this == NONE || this == UNINSTALL;
        }

        public boolean canTick() {
            return this != NONE;
        }
    }

    public static class Builder {

        private final Map<ComponentType<?>, Component> typeMap = new Reference2ReferenceArrayMap<>();
        private final Map<ComponentType<?>, IComponentsChangeListener<? extends Component>> changeListenerMap = new HashMap<>();

        public <T extends Component> Builder component(ComponentType<T> type) {
            return component(type, (c, s) -> {});
        }

        public <T extends Component> Builder component(ComponentType<T> type, IComponentsChangeListener<T> changeListener) {
            return component(ComponentManager.getInstance().getComponent(type.getBaseComponentId()), changeListener);
        }

        public <T extends Component> Builder component(T component, IComponentsChangeListener<T> changeListener) {
            ComponentType<?> type = component.getType();

            typeMap.put(type, component);
            changeListenerMap.put(type, changeListener);
            return this;
        }

        public ComponentStorage build() {
            ImmutableMap.Builder<ComponentType<?>, Entry> entryMap = new ImmutableMap.Builder<>();

            for (Map.Entry<ComponentType<?>, Component> entry : typeMap.entrySet()) {
                ComponentType<?> type = entry.getKey();
                Component component = entry.getValue();
                IComponentsChangeListener<Component> listener = (IComponentsChangeListener<Component>) changeListenerMap.get(type);

                entryMap.put(type, new Entry(listener, component));
            }

            return new ComponentStorage(entryMap.build());
        }

    }

    public static class Entry {
        private final IComponentsChangeListener<Component> callback;
        private Component component;
        private ItemStack stack;

        private Entry(IComponentsChangeListener<Component> callback, Component component) {
            this.callback = callback;
            this.component = component;
            this.stack = createItemStack();
        }

        private void init() {
            callback.onComponentsChanged(component, stack);
        }

        private void update(Component component, ItemStack stack) {
            this.component = component;
            this.stack = stack == null || stack.isEmpty() ? createItemStack() : stack;

            callback.onComponentsChanged(component, this.stack);
        }

        private ItemStack createItemStack() {
            return new ItemStack(component);
        }

        private boolean match(Component component, ItemStack stack) {
            return this.component == component && ItemStack.areItemStacksEqual(this.stack, stack);
        }

        private boolean isBase() {
            return component.isBase() && ItemStack.areItemStacksEqual(this.stack, createItemStack());
        }

        public ItemStack getItemStack() {
            return stack;
        }

        public Component getComponent() {
            return component;
        }
    }
}
