package io.github.ramboxeu.techworks.common.util.cable.network;

import io.github.ramboxeu.techworks.common.util.cable.connection.ConnectionMode;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Map;

public interface INode {
    default Collection<INode> getNeighbours() {
        return getNeighboursMap().values();
    }

    Map<Direction, INode> getNeighboursMap();
    BlockPos getPosition();
    boolean isTraversable();
    int getTransferTime();
    ConnectionMode getConnectionMode(Direction side);

     void onPacketArrived(ICablePacket packet);
     void onPacketDeparted(ICablePacket packet);

    void setHolder(ICableNetworkHolder holder); // Hack
    void removeNeighbour(Direction side); // Also hack
}
