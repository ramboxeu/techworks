package io.github.ramboxeu.techworks.client.container;

import com.mojang.datafixers.util.Pair;
import io.github.ramboxeu.techworks.client.container.sync.IExtendedContainerListener;
import io.github.ramboxeu.techworks.client.container.sync.ObjectHolder;
import io.github.ramboxeu.techworks.client.container.sync.PlayerListenerWrapper;
import io.github.ramboxeu.techworks.client.gui.element.GuiElement;
import io.github.ramboxeu.techworks.client.screen.widget.BaseContainerWidget;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainer extends Container {
    private final List<ObjectHolder<?>> trackedObjects = new ArrayList<>();
    private final List<IExtendedContainerListener> extendedListeners = new ArrayList<>();
    private final List<GuiElement> elements = new ArrayList<>();
    private final List<BaseContainerWidget> widgets = new ArrayList<>();
    private final List<ToggleableSlot> toggleableSlots = new ArrayList<>();

    public BaseContainer(@Nullable ContainerType<?> type, int id) {
        super(type, id);
    }

    protected <T extends BaseContainerWidget> T addWidget(T widget) {
        widgets.add(widget);

        BaseContainerWidget.Builder builder = new BaseContainerWidget.Builder(this::addToggleableSlot);
        widget.init(this, builder);

        builder.getIntTrackers().forEach(this::trackInt);
        builder.getObjectTrackers().forEach(this::trackObject);
        builder.getSlots().forEach(this::addSlot);

        return widget;
    }

    protected <T> ObjectHolder<T> trackObject(ObjectHolder<T> holder) {
        trackedObjects.add(holder);
        return holder;
    }

    protected ToggleableSlot addToggleableSlot(Slot delegate) {
        ToggleableSlot slot = new ToggleableSlot(delegate, this);
        addSlot(slot);
        toggleableSlots.add(slot);
        return slot;
    }

    public void addListener(IExtendedContainerListener listener) {
        if (!extendedListeners.contains(listener)) {
            extendedListeners.add(listener);
            detectAndSendChanges();
        }
    }

    public void buttonClicked(BaseContainerWidget widget, int buttonId) {
        buttonClicked(widgets.indexOf(widget), buttonId);
    }

    public void buttonClicked(int buttonId) {
        buttonClicked(-1, buttonId);
    }

    public final void buttonClicked(int widgetId, int buttonId) {
        TechworksPacketHandler.sendContainerButtonClicked(widgetId, buttonId);
    }

    public final void onButtonClicked(int widgetId, int buttonId) {
        if (widgetId == -1) {
            onButtonClicked(buttonId);
        } else {
            BaseContainerWidget widget = widgets.get(widgetId);

            if (widget != null) {
                widget.onButtonClicked(buttonId);
            }
        }
    }

    protected void onButtonClicked(int buttonId) {}

    private void toggleSlot(ToggleableSlot slot, boolean enable) {
        int slotId = toggleableSlots.indexOf(slot);

        if (slotId > -1) {
            TechworksPacketHandler.syncTaggableSlotState(slotId, enable);
        }
    }

    public final void onToggleSlot(int slotId, boolean enable) {
        ToggleableSlot slot = toggleableSlots.get(slotId);

        if (slot != null) {
            slot.setState(enable, false);
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0, size = trackedObjects.size(); i < size; i++) {
            ObjectHolder<?> holder = trackedObjects.get(i);

            if (holder != null && holder.isDirty()) {
                for (IExtendedContainerListener listener : extendedListeners) {
                    listener.sendObjectUpdate(i, holder);
                }
            }
        }
    }

    protected void addPlayerListener(PlayerEntity player) {
        PlayerListenerWrapper.wrap(player, this::addListener);
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    public List<BaseContainerWidget> getWidgets() {
        return widgets;
    }

    public void updateObjectHolder(int holderId, ObjectHolder<?> synced) {
        ObjectHolder<?> holder = trackedObjects.get(holderId);

        if (holder != null) {
            holder.setTo(synced);
        }
    }

    public static class ToggleableSlot extends Slot {
        private static final IInventory EMPTY = new Inventory(0);

        private final Slot delegate;
        private final BaseContainer owner;

        private boolean enabled = true;

        private ToggleableSlot(Slot delegate, BaseContainer owner) {
            super(EMPTY, 0, delegate.xPos, delegate.yPos);
            this.delegate = delegate;
            this.owner = owner;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            setState(enabled, true);
        }

        public final void setState(boolean enabled, boolean sync) {
            this.enabled = enabled;

            if (enabled) {
                if (!owner.inventorySlots.contains(this)) {
                    slotNumber = owner.inventorySlots.size();
                    owner.inventorySlots.add(this);
                }
            } else {
                owner.inventorySlots.remove(this);
            }

            if (sync) {
                owner.toggleSlot(this, enabled);
            }
        }

        @Override
        public void onSlotChange(ItemStack oldStack, ItemStack newStack) {
            delegate.onSlotChange(oldStack, newStack);
        }

        @Override
        public ItemStack onTake(PlayerEntity player, ItemStack stack) {
            return delegate.onTake(player, stack);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return enabled && delegate.isItemValid(stack);
        }

        @Override
        public ItemStack getStack() {
            return enabled ? delegate.getStack() : ItemStack.EMPTY;
        }

        @Override
        public boolean getHasStack() {
            return enabled && delegate.getHasStack();
        }

        @Override
        public void putStack(ItemStack stack) {
            delegate.putStack(stack);
        }

        @Override
        public void onSlotChanged() {
            delegate.onSlotChanged();
        }

        @Override
        public int getSlotStackLimit() {
            return enabled ? delegate.getSlotStackLimit() : 0;
        }

        @Override
        public int getItemStackLimit(ItemStack stack) {
            return enabled ? delegate.getItemStackLimit(stack) : 0;
        }

        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getBackground() {
            return delegate.getBackground();
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            return delegate.decrStackSize(amount);
        }

        @Override
        public boolean canTakeStack(PlayerEntity player) {
            return enabled && delegate.canTakeStack(player);
        }

        @Override
        public int getSlotIndex() {
            return delegate.getSlotIndex();
        }

        @Override
        public boolean isSameInventory(Slot other) {
            return delegate.isSameInventory(other);
        }

        @Override
        public Slot setBackground(ResourceLocation atlas, ResourceLocation sprite) {
            return delegate.setBackground(atlas, sprite);
        }
    }
}
