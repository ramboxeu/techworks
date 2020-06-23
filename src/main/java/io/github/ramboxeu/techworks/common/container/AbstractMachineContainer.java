package io.github.ramboxeu.techworks.common.container;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.sync.Event;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import io.github.ramboxeu.techworks.common.network.NetworkManager;
import io.github.ramboxeu.techworks.common.registry.TechworksRegistries;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMachineContainer<T extends AbstractMachineBlockEntity> extends ContainerBase {
//    protected List<Integer> syncedValues;
//    protected List<IAutoSyncable> syncableList;

    protected T blockEntity;
    protected PlayerInventory playerInventory;

    //private int nextEmitterId = 0;
    private final List<Integer> observerIds = new ArrayList<>();
    private final Object[] data;
    private List<EventEmitter> emitters = new ArrayList<>();

    public AbstractMachineContainer(int syncId, PlayerInventory playerInventory, T blockEntity, int dataSize) {
        super(syncId);
        this.blockEntity = blockEntity;
        this.playerInventory = playerInventory;

//        syncableList = blockEntity.getComponentList().stream()
//                .filter(component -> component instanceof IAutoSyncable)
//                .map(component -> (IAutoSyncable) component)
//                .collect(Collectors.toList());
//
//        syncedValues = new ArrayList<>();
//
//        this.addProperties(new PropertyDelegate() {
//            @Override
//            public int get(int index) {
//                if (syncableList.size() > index) {
//                    return syncableList.get(index).getValue();
//                } else {
//                    return 0;
//                }
//            }
//
//            @Override
//            public void set(int index, int value) {
//                if (syncedValues.size() > index) {
//                    syncedValues.set(index, value);
//                } else {
//                    syncedValues.add(value);
//                }
//            }
//
//            @Override
//            public int size() {
//                return syncableList.size() + 1;
//            }
//        });

        // We need to have matching sizes on both sides so we don't crash everything
        data = new Object[dataSize];
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public ComponentInventory getComponentInventory() {
        return this.blockEntity.getComponentList();
    }

    public List<Widget> getWidgets() {
        return blockEntity.getWidgets();
    }

//    public int getSyncedValueOrDefault(int index, int defaultValue) {
//        if (syncedValues.size() > index) {
//            return syncedValues.get(index);
//        } else {
//            return defaultValue;
//        }
//    }
//
//    public Optional<Integer> getSyncedValue(int index) {
//        if (syncedValues.size() > index) {
//            return Optional.of(syncedValues.get(index));
//        } else {
//            return Optional.empty();
//        }
//    }

    public T getBlockEntity() {
        return blockEntity;
    }

    public Optional<Object> getData(int index) {
        if (data.length > index) {
            Object value = data[index];
            return value == null ? Optional.empty() : Optional.of(value);
        } else {
            return Optional.empty();
        }
    }

    public void syncData(int dataId, Identifier eventId, CompoundTag tag) {
//        Techworks.LOG.info("Syncing #{}: {}", dataId, TechworksRegistries.EVENT.get(eventId).deserialize(tag));
        Event event = TechworksRegistries.EVENT.get(eventId);
        if (event == null) {
            data[dataId] = null;
        } else {
            data[dataId] = event.deserialize(tag);
        }
    }

    public void subscribeEmitters() {
        emitters = blockEntity.getComponentList()
                .stream()
                .filter(component -> component instanceof EventEmitter)
                .map(component -> (EventEmitter) component)
                .collect(Collectors.toList());

        for (int i = 0; i < emitters.size(); i++) {
            EventEmitter emitter = emitters.get(i);
            int dataId = i;
            observerIds.add(emitter.subscribe(value -> {
//                Techworks.LOG.info("Value changed: {id={}, syncId={}, value={}}", dataId, syncId, emitter.getEvent().serialize(new CompoundTag(), value));
                NetworkManager.sendToPlayer(playerInventory.player, NetworkManager.CONTAINER_DATA_SYNC, buf -> {
                        buf.writeByte(syncId);
                        buf.writeShort(dataId);
                        buf.writeIdentifier(TechworksRegistries.EVENT.getId(emitter.getEvent()));
                        buf.writeCompoundTag(emitter.getEvent().serialize(new CompoundTag(), value));
                    });
            }));
        }
    }

    public void unsubscribeEmitters() {
        if (!observerIds.isEmpty() && !emitters.isEmpty()) {
            for (int i = 0; i < emitters.size(); i++) {
                emitters.get(i).unsubscribe(observerIds.get(i));
            }
        }
    }
}
