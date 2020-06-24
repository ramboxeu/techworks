package io.github.ramboxeu.techworks.common.api.sync;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public abstract class EventEmitter {
    private final ArrayList<Observer> observers;
    private final Event event;

    protected EventEmitter(Event event) {
        observers = new ArrayList<>();
        this.event = event;
    }

    public int subscribe(Observer observer) {
        observers.add(observer);
        return observers.size() - 1;
    }

    public void unsubscribe(int id) {
        observers.remove(id);
    }

    public void unsubscribe(Observer id) {
        observers.remove(id);
    }

    protected void updateAll(Object value) {
        observers.forEach(observer -> observer.update(value));
    }

    public Event getEvent() {
        return event;
    }

    public interface Observer {
        void update(Object value);
    }
}