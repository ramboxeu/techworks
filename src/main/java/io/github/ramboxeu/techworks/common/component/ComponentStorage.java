package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ComponentStorage implements IItemHandler, INBTSerializable<CompoundNBT> {

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

    private CompoundNBT serializeStorage() {
        CompoundNBT tag = new CompoundNBT();

        for (Map.Entry<ComponentType<?>, Entry> entry : storage.entrySet()) {
            ComponentType<?> type = entry.getKey();
            ResourceLocation id = entry.getValue().component.getId();
            ResourceLocation typeId = TechworksRegistries.COMPONENT_TYPES.getKey(type);

            tag.putString(typeId.toString(), id.toString());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        waitTimer = tag.getInt("WaitTimer");
        finished = tag.getBoolean("Finished");
        pendingStack = ItemStack.read(tag.getCompound("PendingStack"));
        deserializeStorage(tag.getCompound("Storage"));

        operation = NBTUtils.deserializeEnum(tag, "Operation", Operation.class);

        if (operation == null)
            operation = Operation.NONE;

        pendingComponent = NBTUtils.deserializeComponentType(tag, "PendingComponent");
    }

    private void deserializeStorage(CompoundNBT tag) {
        for (String key : tag.keySet()) {
            String value = tag.getString(key);

            ComponentType<?> type = TechworksRegistries.COMPONENT_TYPES.getValue(new ResourceLocation(key));
            Component component = ComponentManager.getInstance().getComponent(new ResourceLocation(value));

            if (component != null) {
                storage.get(type).setComponent(component);
            }
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
                if (entry.component.isBase())
                    return false;

                if (operation == Operation.UNINSTALL)
                    waitTimer = 0;

                finished = false;
                pendingComponent = type;
                pendingStack = entry.createItemStack();
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
        return Collections.unmodifiableSet(storage.keySet());
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
            return storage.values().stream().noneMatch(entry -> entry.component == component)
                    && storage.get(component.getType()).component.isBase()
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
            entry.setComponent(component);
        }

        pendingStack = ItemStack.EMPTY;
        finished = false;
    }

    private void extract() {
        Entry entry = storage.get(pendingComponent);
        entry.setComponent(ComponentManager.getInstance().getComponent(pendingComponent.getBaseComponentId()));

        pendingComponent = null;
    }

    public float getOperationProgress() {
        return waitTimer / 200.0f;
    }

    public boolean isProcessing() {
        return operation != Operation.NONE;
    }

    // INTERNAL
    public void putSlotStack(ItemStack stack) {
        pendingStack = stack;
    }

    public Operation getOperation() {
        return operation;
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

        private final Map<ComponentType<?>, Component> typeMap = new HashMap<>();
        private final Map<ComponentType<?>, Consumer<? extends Component>> changeListenerMap = new HashMap<>();

        public <T extends Component> Builder component(ComponentType<T> type) {
            return component(type, c -> {});
        }

        public <T extends Component> Builder component(ComponentType<T> type, Consumer<T> changeListener) {
            return component(Objects.requireNonNull(ComponentManager.getInstance().getComponent(type.getBaseComponentId())), changeListener);
        }

        public <T extends Component> Builder component(T component, Consumer<T> changeListener) {
            ComponentType<?> type = component.getType();

            typeMap.put(type, component);
            changeListenerMap.put(type, changeListener);
            return this;
        }

        public ComponentStorage build() {
            Map<ComponentType<?>, Entry> entryMap = new HashMap<>();

            for (Map.Entry<ComponentType<?>, Component> entry : typeMap.entrySet()) {
                ComponentType<?> type = entry.getKey();
                Component component = entry.getValue();
                Consumer<Component> listener = (Consumer<Component>) changeListenerMap.get(type);

                entryMap.put(type, new Entry(listener, component));
            }

            return new ComponentStorage(Collections.unmodifiableMap(entryMap));
        }

    }

    private static class Entry {
        private final Consumer<Component> callback;
        private Component component;

        private Entry(Consumer<Component> callback, Component component) {
            this.callback = callback;
            this.component = component;
        }

        private void init() {
            callback.accept(component);
        }

        private void setComponent(Component component) {
            this.component = component;
            callback.accept(component);
        }

        public ItemStack createItemStack() {
            return new ItemStack(component);
        }
    }
}
