package io.github.ramboxeu.techworks.common.util.cable.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public interface IWrapperEndpointNode extends IEndpointNode {
    void addNeighbour(INode node, Direction side);
    void removeNeighbour(INode node);
    void removeNeighbour(Direction side);

    @Override
    default void onPacketArrived(ICablePacket packet) {}

    @Override
    default void onPacketDeparted(ICablePacket packet) {}

    @FunctionalInterface
    interface Factory {
        IWrapperEndpointNode create(TileEntity tile);
    }
}
