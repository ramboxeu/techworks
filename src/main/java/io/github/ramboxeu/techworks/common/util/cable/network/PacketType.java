package io.github.ramboxeu.techworks.common.util.cable.network;

import io.github.ramboxeu.techworks.common.util.cable.item.ItemPacket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;

import java.util.function.Function;

public enum PacketType {
    ITEM(PacketType::decodeItem),
    LIQUID(null),
    GAS(null),
    ENERGY(null);

    private final Function<PacketBuffer, ICablePacket> decoder;

    PacketType(Function<PacketBuffer, ICablePacket> decoder) {
        this.decoder = decoder;
    }

    public ICablePacket decode(PacketBuffer buffer) {
        return decoder.apply(buffer);
    }

    private static ICablePacket decodeItem(PacketBuffer buffer) {
        Direction next = buffer.readEnumValue(Direction.class);
        Direction previous = buffer.readEnumValue(Direction.class);
        ItemStack stack = buffer.readItemStack();
        int time = buffer.readByte();

        return new ItemPacket(previous, next, stack, time);
    }
}
