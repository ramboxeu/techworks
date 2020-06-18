package io.github.ramboxeu.techworks.common.api.sync;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public abstract class EventEmitter {
    private final ArrayList<Observer> observers;

    protected EventEmitter() {
        observers = new ArrayList<>();
    }

    public int subscribe(Observer observer) {
        observers.add(observer);
        return observers.size() - 1;
    }

    public void unsubscribe(int id) {
        observers.remove(id);
    }

    protected void updateAll(Object value) {
        observers.forEach(observer -> observer.update(value));
    }

    public abstract CompoundTag serialize(CompoundTag tag, Object value);

    public abstract Object deserialize(CompoundTag tag);

    public interface Observer {
        void update(Object value);
    }
}
