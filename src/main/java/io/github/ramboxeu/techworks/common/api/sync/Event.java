package io.github.ramboxeu.techworks.common.api.sync;

import net.minecraft.nbt.CompoundTag;

public class Event {
    private final Serializer serializer;
    private final Deserializer deserializer;

    public Event(Serializer serializer, Deserializer deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public CompoundTag serialize(CompoundTag tag, Object object) {
        return serializer.serialize(tag, object);
    }

    public Object deserialize(CompoundTag tag) {
        return deserializer.deserialize(tag);
    }

    public interface Serializer {
        CompoundTag serialize(CompoundTag tag, Object value);
    }

    public interface Deserializer {
        Object deserialize(CompoundTag tag);
    }
}
