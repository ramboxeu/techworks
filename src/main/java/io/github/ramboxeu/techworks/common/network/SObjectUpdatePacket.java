package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SObjectUpdatePacket {
    private int windowId;
    private int objectId;
    private CompoundNBT tag;

    public SObjectUpdatePacket(int windowId, int objectId, CompoundNBT tag) {
        this.windowId = windowId;
        this.objectId = objectId;
        this.tag = tag;
    }


    public static SObjectUpdatePacket decode(PacketBuffer buf) {
        return new SObjectUpdatePacket(buf.readUnsignedByte(), buf.readInt(), buf.readCompoundTag());
    }

    public static void encode(SObjectUpdatePacket packet, PacketBuffer buf) {
        buf.writeByte(packet.windowId);
        buf.writeInt(packet.objectId);
        buf.writeCompoundTag(packet.tag);
    }

    public static void handle(SObjectUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Container container = Minecraft.getInstance().player.openContainer;
            if (container != null && container.windowId == packet.windowId && container instanceof BaseMachineContainer<?>) {
                ((BaseMachineContainer<?>) container).updateObject(packet.objectId, packet.tag);
            }
        });
        context.get().setPacketHandled(true);
    }
}
