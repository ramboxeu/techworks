package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.capability.CapabilityExtendedListenerProvider;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.extensions.IForgeChunk;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineContainer<T extends BaseMachineTile> extends Container {
    protected IItemHandler playerInventory;
    protected T machineTile;

    private final List<ObjectReferenceHolder> trackedObjects = new ArrayList<>();
    private final List<IExtendedContainerListener> extendedListeners = new ArrayList<>();

    protected BaseMachineContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, T machineTile) {
        super(containerType, id);

        this.machineTile = machineTile;
        this.playerInventory = new InvWrapper(playerInventory);

        if (playerInventory.player instanceof ServerPlayerEntity) {
            playerInventory.player.getCapability(CapabilityExtendedListenerProvider.EXTENDED_LISTENER_PROVIDER).ifPresent(provider ->
                    addListener(provider.create((ServerPlayerEntity) playerInventory.player)));
        }

        this.layoutPlayerSlots();
    }

    protected Slot addSlot(SlotBuilder slotBuilder) {
        return addSlot(slotBuilder.build());
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

    private void layoutPlayerSlots() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, 8 + i *18, 142));
        }
    }
}
