package io.github.ramboxeu.techworks.client.container;

import net.minecraft.nbt.CompoundNBT;

public abstract class ObjectReferenceHolder {
    private Object oldValue;

    public abstract Object get();

    public abstract CompoundNBT serialize(Object value);

    public abstract void set(Object value);

    public abstract Object deserialize(CompoundNBT tag);

    protected abstract boolean isSame(Object old, Object current);

    public boolean isDirty() {
        Object newValue = get();
        boolean isDirty = oldValue == null || !isSame(oldValue, newValue);
        oldValue = newValue;
        return isDirty;
    }
}
