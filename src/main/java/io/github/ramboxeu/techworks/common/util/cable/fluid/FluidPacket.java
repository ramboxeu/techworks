package io.github.ramboxeu.techworks.common.util.cable.fluid;

import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.IEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.NetworkType;
import io.github.ramboxeu.techworks.common.util.cable.network.PacketType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;

public class FluidPacket implements ICablePacket {

    private final IEndpointNode node;
    private final PacketType type;
    private final FluidStack stack;
    private final Direction side;

    private int time;

    private FluidPacket(IEndpointNode node, PacketType type, FluidStack stack, Direction side, int time) {
        this.node = node;
        this.type = type;
        this.stack = stack;
        this.side = side;
        this.time = time;
    }

    public void tick() {
        time -= 1;
    }

    public boolean isReady() {
        return time == 0;
    }

    public void transport() {
        node.insert(this, side);
    }

    @Override
    public PacketType getType() {
        return type;
    }

    @Override
    public Object getPayload() {
        return stack;
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {}

    public static FluidPacket create(NetworkType type, IEndpointNode node, FluidStack stack, Direction side, int time) {
        if (type == NetworkType.LIQUID) {
            return new FluidPacket(node, PacketType.LIQUID, stack, side, time);
        }

        if (type == NetworkType.GAS) {
            return new FluidPacket(node, PacketType.GAS, stack, side, time);
        }

        throw new IllegalArgumentException("Expected LIQUID or GAS type");
    }

    public int getTime() {
        return this.time;
    }
}
