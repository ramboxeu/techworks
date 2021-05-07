package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.client.gui.element.GuiElement;
import io.github.ramboxeu.techworks.client.screen.widget.BaseContainerWidget;
import io.github.ramboxeu.techworks.common.capability.CapabilityExtendedListenerProvider;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseContainer extends Container {
    private final List<ObjectReferenceHolder> trackedObjects = new ArrayList<>();
    private final List<IExtendedContainerListener> extendedListeners = new ArrayList<>();
    private final List<GuiElement> elements = new ArrayList<>();
    private final List<BaseContainerWidget> widgets = new ArrayList<>();

    public BaseContainer(@Nullable ContainerType<?> type, int id) {
        super(type, id);
    }

    protected <T extends BaseContainerWidget> T addWidget(T widget) {
        widgets.add(widget);

        BaseContainerWidget.Builder builder = new BaseContainerWidget.Builder();
        widget.init(this, builder);

        builder.getIntTrackers().forEach(this::trackInt);
        builder.getObjectTrackers().forEach(this::trackObject);
        builder.getSlots().forEach(this::addSlot);

        return widget;
    }

    protected ObjectReferenceHolder trackObject(ObjectReferenceHolder holder) {
        trackedObjects.add(holder);
        return holder;
    }

    public void updateObject(int objectId, CompoundNBT tag) {
        ObjectReferenceHolder holder = trackedObjects.get(objectId);
        holder.set(holder.deserialize(tag));
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

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < trackedObjects.size(); i++) {
            ObjectReferenceHolder holder = trackedObjects.get(i);
            if (holder.isDirty()) {
                for (IExtendedContainerListener listener : extendedListeners) {
                    listener.sendObjectUpdate(this, i, holder.serialize(holder.get()));
                }
            }
        }
    }

    protected void addPlayerListener(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            player.getCapability(CapabilityExtendedListenerProvider.EXTENDED_LISTENER_PROVIDER)
                    .ifPresent(provider -> addListener(provider.create((ServerPlayerEntity) player)));
        }
    }

    public List<GuiElement> getElements() {
        return elements;
    }

    public List<BaseContainerWidget> getWidgets() {
        return widgets;
    }
}
