package io.github.ramboxeu.techworks.common.util.cable.energy;

import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.PacketType;
import net.minecraft.network.PacketBuffer;

public class EnergyPacket implements ICablePacket {
    private final int energy;

    public EnergyPacket(int energy) {
        this.energy = energy;
    }

    @Override
    public PacketType getType() {
        return PacketType.ENERGY;
    }

    @Override
    public Object getPayload() {
        return energy;
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {

    }
}
