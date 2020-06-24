package io.github.ramboxeu.techworks.common.container;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMachineContainer<T extends AbstractMachineBlockEntity> extends ContainerBase {
    protected T blockEntity;
    protected PlayerInventory playerInventory;

    private final Object[] data;

    public AbstractMachineContainer(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, T blockEntity, int dataSize) {
        super(type, syncId, blockEntity.getWidgets());
        this.blockEntity = blockEntity;
        this.playerInventory = playerInventory;

        // We need to have matching sizes on both sides so we don't crash everything
        // Probably good idea is to move this to BE and then send everything ready
        data = new Object[dataSize];

        List<EventEmitter> emitters = blockEntity.getComponentList()
                .stream()
                .filter(component -> component instanceof EventEmitter)
                .map(component -> (EventEmitter) component)
                .collect(Collectors.toList());

        for (int i = 0; i < emitters.size(); i++) {
            int dataId = i;
            subscribe(emitters.get(i), value -> data[dataId] = value);
        }
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

//    public void syncData(int dataId, Identifier eventId, CompoundTag tag) {
////        Techworks.LOG.info("Syncing #{}: {}", dataId, TechworksRegistries.EVENT.get(eventId).deserialize(tag));
//        Event event = TechworksRegistries.EVENT.get(eventId);
//        if (event == null) {
//            data[dataId] = null;
//        } else {
//            data[dataId] = event.deserialize(tag);
//        }
//    }

//    public void subscribeEmitters() {
//        emitters = blockEntity.getComponentList()
//                .stream()
//                .filter(component -> component instanceof EventEmitter)
//                .map(component -> (EventEmitter) component)
//                .collect(Collectors.toList());
//
//        for (int i = 0; i < emitters.size(); i++) {
//            EventEmitter emitter = emitters.get(i);
//            int dataId = i;
//            observerIds.add(emitter.subscribe(value -> {
////                Techworks.LOG.info("Value changed: {id={}, syncId={}, value={}}", dataId, syncId, emitter.getEvent().serialize(new CompoundTag(), value));
//                NetworkManager.sendToPlayer(playerInventory.player, NetworkManager.CONTAINER_DATA_SYNC, buf -> {
//                        buf.writeByte(syncId);
//                        buf.writeShort(dataId);
//                        buf.writeIdentifier(TechworksRegistries.EVENT.getId(emitter.getEvent()));
//                        buf.writeCompoundTag(emitter.getEvent().serialize(new CompoundTag(), value));
//                    });
//            }));
//        }
//    }

//    public void unsubscribeEmitters() {
//        if (!observerIds.isEmpty() && !emitters.isEmpty()) {
//            for (int i = 0; i < emitters.size(); i++) {
//                emitters.get(i).unsubscribe(observerIds.get(i));
//            }
//        }
//    }
}
