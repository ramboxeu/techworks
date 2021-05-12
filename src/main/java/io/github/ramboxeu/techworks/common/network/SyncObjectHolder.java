package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.sync.ObjectHolder;
import io.github.ramboxeu.techworks.client.container.sync.ObjectHolderSerializers;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncObjectHolder {
    private final int holderId;
    private final ObjectHolder<?> holder;

    public SyncObjectHolder(int holderId, ObjectHolder<?> holder) {
        this.holderId = holderId;
        this.holder = holder;
    }

    public static void encode(SyncObjectHolder packet, PacketBuffer buffer) {
        ObjectHolder<?> holder = packet.holder;
        buffer.writeShort(holder.getSerializer().getId());
        buffer.writeShort(packet.holderId);
        holder.encode(buffer);
    }

    public static SyncObjectHolder decode(PacketBuffer buffer) {
        int serializerId = buffer.readShort();
        int holderId = buffer.readShort();

        ObjectHolder<?> holder = ObjectHolderSerializers.getSerializer(serializerId).decode(buffer);
        return new SyncObjectHolder(holderId, holder);
    }

    public static void handle(SyncObjectHolder packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Container container = Minecraft.getInstance().player.openContainer;

            if (container instanceof BaseContainer) {
                ((BaseContainer) container).updateObjectHolder(packet.holderId, packet.holder);
            }
        });
        context.get().setPacketHandled(true);
    }
}
