package io.github.ramboxeu.techworks.common.util;

import net.minecraft.network.PacketBuffer;

public interface IPacketSerializable {
    void serializePacket(PacketBuffer buffer);
    void deserializePacket(PacketBuffer buffer);
}
