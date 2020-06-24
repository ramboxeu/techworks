package io.github.ramboxeu.techworks.common.container;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.registry.TechworksRegistries;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class ContainerBase extends ScreenHandler {
    private final List<EventEmitter.Observer> observers;
    private final List<EventEmitter> emitters;
    private final List<Consumer<Object>> consumers;

    protected final List<Widget> widgets;

    public ContainerBase(ScreenHandlerType<?> type, int syncId, List<Widget> widgets) {
        super(type, syncId);

        observers = new ArrayList<>();
        emitters = new ArrayList<>();
        consumers = new ArrayList<>();

        this.widgets = widgets;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        unsubscribeAll();
    }

    // Maybe strip the subscription stuff on the client only add consumers
    // and vice-versa on the server
    protected void subscribe(EventEmitter emitter, EventEmitter.Observer observer, Consumer<Object> consumer) {
        emitter.subscribe(observer);
        emitters.add(emitter);
        observers.add(observer);
        consumers.add(consumer);
    }

    protected void subscribe(EventEmitter emitter, Consumer<Object> consumer) {
        subscribe(
                emitter,
                value -> {
                    // Sync packet
                    Techworks.LOG.info("{}", value);
                },
                consumer
        );
    }

    protected void unsubscribe(EventEmitter emitter, EventEmitter.Observer observer) {
        emitter.unsubscribe(observer);
        emitters.remove(emitter);
        observers.remove(observer);
    }

    protected void unsubscribeAll() {
        if (!observers.isEmpty() && !emitters.isEmpty()) {
            for (int i = 0; i < emitters.size(); i++) {
                unsubscribe(emitters.get(i), observers.get(i));
            }
        }
    }

    public void syncValue(Identifier eventId, int consumerId, CompoundTag tag) {
        Object value = TechworksRegistries.EVENT.get(eventId);
        consumers.get(consumerId).accept(value);
    }

    public List<Widget> getWidgets() {
        return widgets;
    }
}
