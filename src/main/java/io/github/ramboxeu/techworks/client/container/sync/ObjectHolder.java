package io.github.ramboxeu.techworks.client.container.sync;

import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public abstract class ObjectHolder<T> {
    private final ObjectHolderSerializer<T> serializer;
    protected T oldValue;

    public ObjectHolder(ObjectHolderSerializer<T> serializer) {
        this.serializer = serializer;
//        oldValue = get();
    }

    protected ObjectHolder(ObjectHolderSerializer<T> serializer, T value) {
        this.serializer = serializer;
        this.oldValue = value;
    }

    public abstract T get();
    public abstract void set(@Nullable T value);
    protected abstract T copy(T value);

    public ObjectHolderSerializer<T> getSerializer() {
        return serializer;
    }

    protected boolean areEqual(@Nullable T oldValue, @Nullable T newValue) {
        if (oldValue == null) {
            return newValue == null;
        }

        return oldValue.equals(newValue);
    }

    public final void setTo(ObjectHolder<?> other) {
        oldValue = (T) other.oldValue;
        set(oldValue);
    }

    public final boolean isDirty() {
        T value = get();
        boolean flag = areEqual(oldValue, value);

        if (!flag) {
            oldValue = copy(value);
            return true;
        }

        return false;
    }

    public final void encode(PacketBuffer buffer) {
        serializer.encode(this, buffer);
    }

}
