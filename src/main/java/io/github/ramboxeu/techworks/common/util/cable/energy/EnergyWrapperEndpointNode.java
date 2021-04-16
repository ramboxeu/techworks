package io.github.ramboxeu.techworks.common.util.cable.energy;

import io.github.ramboxeu.techworks.common.util.HandlerStorage;
import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import io.github.ramboxeu.techworks.common.util.cable.network.IWrapperEndpointNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class EnergyWrapperEndpointNode implements IWrapperEndpointNode {

    private final TileEntity tile;
    private final HandlerStorage<IEnergyStorage> handlers;
    private final Map<Direction, INode> neighbors;

    public EnergyWrapperEndpointNode(TileEntity tile) {
        this.tile = tile;

        neighbors = new EnumMap<>(Direction.class);
        handlers = new HandlerStorage<>(CapabilityEnergy.ENERGY);
    }

    @Override
    public TileEntity getTileEntity() {
        return tile;
    }

    @Override
    public boolean isEmpty() {
        return handlers.stream().noneMatch(handler -> handler.getEnergyStored() != 0);
    }

    @Override
    public boolean isFull() {
        return handlers.stream().allMatch(handler -> handler.getEnergyStored() == handler.getMaxEnergyStored());
    }

    @Override
    public Object insert(ICablePacket packet, Direction side) {
        if (packet instanceof EnergyPacket) {
            return handlers.get(side.getOpposite()).receiveEnergy((Integer) packet.getPayload(), false);
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

        handlers.store(side.getOpposite(), tile);
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
        neighbors.remove(side);
        handlers.remove(side);
    }
}
