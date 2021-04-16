package io.github.ramboxeu.techworks.common.util.cable.network;

import io.github.ramboxeu.techworks.common.util.cable.connection.ConnectionMode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public interface IEndpointNode extends INode {
    TileEntity getTileEntity();
    boolean isEmpty();
    boolean isFull();

     Object insert(ICablePacket packet, Direction side);

    @Override
    default boolean isTraversable() {
        return false;
    }

    @Override
    default int getTransferTime() {
        return 0;
    }

    @Override
    default ConnectionMode getConnectionMode(Direction side) {
        return ConnectionMode.BOTH;
    }

    @Override default void setHolder(ICableNetworkHolder holder) {}
    @Override default void removeNeighbour(Direction side) {}
}
