package io.github.ramboxeu.techworks.common.util.cable.fluid;

import io.github.ramboxeu.techworks.common.util.Pair;
import io.github.ramboxeu.techworks.common.util.Predicates;
import io.github.ramboxeu.techworks.common.util.cable.network.*;
import io.github.ramboxeu.techworks.common.util.cable.pathfinding.Pathfinding;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class FluidCableNetwork extends BaseCableNetwork {

    private final Queue<FluidPacket> packets = new PriorityQueue<>(Comparator.comparingInt(FluidPacket::getTime));
    private final NetworkType type;
    private Fluid fluid;

    private FluidCableNetwork(NetworkType type, UUID id) {
        super(id);

        this.type = type;
    }

    public FluidCableNetwork(NetworkType type, UUID id, List<IEndpointNode> endpoints) {
        super(id, endpoints);

        this.type = type;
    }

    @Override
    public NetworkType getType() {
        return type;
    }

    @Override
    public void tick() {
        packets.forEach(FluidPacket::tick);

        if (!packets.isEmpty()) {
            while (!packets.isEmpty() && packets.peek().isReady()) {
                packets.poll().transport();
            }

            if (packets.isEmpty()) {
                fluid = null;
            }
        }
    }

    public boolean isFluidValid(FluidStack stack) {
        if (fluid == null)
            return true;

        if (!stack.isEmpty()) {
            if (Predicates.isLiquid(stack))
                return type == NetworkType.LIQUID;

            return stack.getFluid() == fluid;
        }

        return false;
    }

    public void transfer(TransferType type, FluidStack stack, INode node, IEndpointNode origin) {
        IEndpointNode endpoint = getTransferTarget(type, origin);

        if (endpoint == null)
            return;

        Pair<Integer, Direction> pair = Pathfinding.timeAndFinalOffset(node, endpoint);

        if (pair == null)
            return;

        if (fluid == null)
            fluid = stack.getFluid();

        packets.add(FluidPacket.create(this.type, endpoint, stack, pair.getRight(), pair.getLeft()));
    }

    public static FluidCableNetwork liquid(UUID id) {
        return new FluidCableNetwork(NetworkType.LIQUID, id);
    }

    public static FluidCableNetwork liquid(UUID id, List<IEndpointNode> endpoints) {
        return new FluidCableNetwork(NetworkType.LIQUID, id, endpoints);
    }

    public static FluidCableNetwork gas(UUID id) {
        return new FluidCableNetwork(NetworkType.GAS, id);
    }

    public static FluidCableNetwork gas(UUID id, List<IEndpointNode> endpoints) {
        return new FluidCableNetwork(NetworkType.GAS, id, endpoints);
    }
}
