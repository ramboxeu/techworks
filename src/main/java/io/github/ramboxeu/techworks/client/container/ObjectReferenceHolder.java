package io.github.ramboxeu.techworks.client.container;

import net.minecraft.nbt.CompoundNBT;

public abstract class ObjectReferenceHolder {
    private Object oldValue;

    public abstract Object get();

    public abstract CompoundNBT serialize(Object value);

    public abstract void set(Object value);

    public abstract Object deserialize(CompoundNBT tag);

    public boolean isDirty() {
        Object newValue = get();
        boolean isDirty = !newValue.equals(oldValue);
        oldValue = newValue;
        return isDirty;
    }
}
