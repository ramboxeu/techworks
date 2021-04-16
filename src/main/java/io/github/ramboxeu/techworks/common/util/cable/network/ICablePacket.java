package io.github.ramboxeu.techworks.common.util.cable.network;

import net.minecraft.network.PacketBuffer;

public interface ICablePacket {
    PacketType getType();
    Object getPayload();
    void writeToPacket(PacketBuffer buffer);
}
