package io.github.ramboxeu.techworks.client.container.sync;

import net.minecraft.network.PacketBuffer;

public abstract class ObjectHolderSerializer<T> {
    private final int id;

    public ObjectHolderSerializer(int id) {
        this.id = id;
    }

    public abstract void encode(ObjectHolder<T> object, PacketBuffer buffer);
    public abstract ObjectHolder<T> decode(PacketBuffer buffer);

    public int getId() {
        return id;
    }
}
