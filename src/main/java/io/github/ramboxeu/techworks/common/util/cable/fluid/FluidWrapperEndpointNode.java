package io.github.ramboxeu.techworks.common.util.cable.fluid;

import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import io.github.ramboxeu.techworks.common.util.cable.network.IWrapperEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.PacketType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class FluidWrapperEndpointNode implements IWrapperEndpointNode {

    private final TileEntity tile;
    private final Map<Direction, INode> neighbors;
    private final Map<Direction, IFluidHandler> handlers;
    private final PacketType type;

    public FluidWrapperEndpointNode(PacketType type, TileEntity tile) {
        this.tile = tile;
        this.type = type;

        neighbors = new EnumMap<>(Direction.class);
        handlers = new EnumMap<>(Direction.class);
    }

    @Override
    public TileEntity getTileEntity() {
        return tile;
    }

    @Override
    public boolean isEmpty() {
        for (IFluidHandler handler : handlers.values()) {
            for (int i = 0; i < handler.getTanks(); i++) {
                if (!handler.getFluidInTank(i).isEmpty())
                    return false;
            }
        }

        return true;
    }

    @Override
    public boolean isFull() {
        for (IFluidHandler handler : handlers.values()) {
            for (int i = 0; i < handler.getTanks(); i++) {
                int amount = handler.getFluidInTank(i).getAmount();
                if (amount < handler.getTankCapacity(i))
                    return false;
            }
        }

        return true;
    }

    @Override
    public Object insert(ICablePacket packet, Direction side) {
        if (packet.getType() == type) {
            IFluidHandler handler = handlers.get(side.getOpposite());

            if (handler != null) {
                FluidStack stack = (FluidStack) packet.getPayload();
                return handler.fill(stack, IFluidHandler.FluidAction.EXECUTE);
            }
        }

        return null;
    }

    @Override
    public Map<Direction, INode> getNeighboursMap() {
        return neighbors;
    }

    @Override
    public BlockPos getPosition() {
        return tile.getPos();
    }

    @Override
    public void addNeighbour(INode node, Direction side) {
        neighbors.put(side.getOpposite(), node);

        tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                .ifPresent(handler -> handlers.put(side.getOpposite(), handler));
    }

    @Override
    public void removeNeighbour(INode node) {
        for (Iterator<Map.Entry<Direction, INode>> iter = neighbors.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Direction, INode> entry = iter.next();

            if (entry.getValue() == node) {
                handlers.remove(entry.getKey());
                iter.remove();
            }
        }
    }

    @Override
    public void removeNeighbour(Direction side) {
        neighbors.remove(side.getOpposite());
        handlers.remove(side.getOpposite());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof TileEntity)
            return tile == obj;

        if (obj instanceof FluidWrapperEndpointNode)
            return tile == ((FluidWrapperEndpointNode) obj).tile;

        return false;
    }


    public static FluidWrapperEndpointNode liquid(TileEntity tile) {
        return new FluidWrapperEndpointNode(PacketType.LIQUID, tile);
    }

    public static IWrapperEndpointNode gas(TileEntity tile) {
        return new FluidWrapperEndpointNode(PacketType.GAS, tile);
    }
}
