package io.github.ramboxeu.techworks.common.util.cable.pathfinding;

import io.github.ramboxeu.techworks.common.util.cable.connection.ConnectionMode;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class NodeWrapper implements Comparable<NodeWrapper> {
    private final INode node;
    private NodeWrapper[] neighbours = null;

    NodeWrapper parent;
    int gCost;
    int hCost;

    NodeWrapper(INode node) {
        this.node = node;
    }

    @Override
    public int compareTo(@Nonnull NodeWrapper node) {
        int result = Integer.compare(fCost(), node.fCost());

        if (result == 0)
            return Integer.compare(hCost, node.hCost);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NodeWrapper) {
            NodeWrapper node = (NodeWrapper) obj;

            if (node.node == this.node) return true;
            return node.pos().equals(pos());
        }

        if (obj instanceof INode) {
            INode node = (INode) obj;

            if (node == this.node) return true;
            return node.getPosition().equals(this.node.getPosition());
        }

        return false;
    }

    int fCost() {
        return gCost + hCost;
    }

    public BlockPos pos() {
        if (node != null)
            return node.getPosition();
        else
            return BlockPos.ZERO;
    }


    NodeWrapper[] neighbours(HashMap<INode, NodeWrapper> wrappers) {
        if (neighbours == null) {
            neighbours = node.getNeighboursMap().entrySet().stream()
                    .filter(e -> canInput(node.getConnectionMode(e.getKey())))
                    .map(e -> wrappers.computeIfAbsent(e.getValue(), NodeWrapper::new))
                    .toArray(NodeWrapper[]::new);
        }

        return neighbours;
    }

    boolean canInput(ConnectionMode mode) {
        return mode.canOutput() || mode.isConnection();
    }

    public INode getNode() {
        return node;
    }

    public boolean isTraversable() {
        return node.isTraversable();
    }

    public int getTime() {
        return node.getTransferTime();
    }
}
