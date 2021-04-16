package io.github.ramboxeu.techworks.common.util.cable.network;

import io.github.ramboxeu.techworks.common.tile.BaseCableTile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseCableNetwork implements ICableNetwork {
    private final UUID id;
    private final List<IEndpointNode> endpoints;
    private int nextRoundRobinTarget = 0;
    private int returnRoundRobinTarget = -1;

    public BaseCableNetwork(UUID id) {
        this.id = id;
        this.endpoints = new ArrayList<>();
    }

    public BaseCableNetwork(UUID id, List<IEndpointNode> endpoints) {
        this.id = id;
        this.endpoints = endpoints;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt("NextRoundRobinTarget", nextRoundRobinTarget);
        tag.putInt("ReturnRoundRobinTarget", returnRoundRobinTarget);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        nextRoundRobinTarget = tag.getInt("NextRoundRobinTarget");
        nextRoundRobinTarget = tag.getInt("ReturnRoundRobinTarget");
    }

    @Nullable
    public IEndpointNode getTransferTarget(TransferType type, IEndpointNode origin) {
        switch (type) {
            case NORMAL:
                return getNormalTransferTarget(origin);
            case ROUND_ROBIN:
                return getRoundRobinTransferTarget(origin);
            case CLOSEST:
                return getClosestTransferTarget(origin);
            case FURTHEST:
                return getFurthestTransferTarget(origin);
        }

        return null;
    }

//    public boolean hasTransferTarget(TransferType type, IEndpointNode origin) {
//        switch (type)
//    }


    private IEndpointNode getClosestTransferTarget(IEndpointNode origin) {
        return endpoints.stream()
                .filter(n -> !n.equals(origin))
                .min(Comparator.comparingInt(n -> n.getPosition().manhattanDistance(origin.getPosition())))
                .orElse(null);
    }

    private IEndpointNode getFurthestTransferTarget(IEndpointNode origin) {
        return endpoints.stream()
                .filter(n -> !n.equals(origin))
                .max(Comparator.comparingInt(n -> n.getPosition().manhattanDistance(origin.getPosition())))
                .orElse(null);
    }

    private IEndpointNode getRoundRobinTransferTarget(IEndpointNode origin) {
        if (nextRoundRobinTarget >= endpoints.size())
            nextRoundRobinTarget = 0;

        boolean ret = returnRoundRobinTarget != -1;
        int index = ret ? returnRoundRobinTarget : nextRoundRobinTarget;

        IEndpointNode node = endpoints.get(index);

        if (node.equals(origin)) {
            returnRoundRobinTarget = index;

            if (ret && nextRoundRobinTarget != index) {
                IEndpointNode next = endpoints.get(nextRoundRobinTarget);
                nextRoundRobinTarget += 1;
                return next;
            } else {
                nextRoundRobinTarget += 1;

                if (nextRoundRobinTarget >= endpoints.size())
                    nextRoundRobinTarget = 0;

                IEndpointNode next = endpoints.get(nextRoundRobinTarget);
                nextRoundRobinTarget += 1;
                return next;
            }
        } else {
            nextRoundRobinTarget += 1;
            returnRoundRobinTarget = -1;
            return node;
        }
    }

    private IEndpointNode getNormalTransferTarget(IEndpointNode origin) {
        return endpoints.stream()
                .filter(node -> !node.isFull() && !node.equals(origin))
                .findAny()
                .orElse(null);
    }

    public IEndpointNode getOrCreateEndpoint(Direction side, TileEntity tile, BaseCableTile cable) {
        Optional<IEndpointNode> optional = endpoints.stream().filter(n -> n.getTileEntity() == tile).findAny();

        if (optional.isPresent()) {
            IEndpointNode endpoint = optional.get();

            if (endpoint instanceof IWrapperEndpointNode)
                ((IWrapperEndpointNode) endpoint).addNeighbour(cable, side);

            return endpoint;
        } else {
            IWrapperEndpointNode.Factory factory = getType().getWrapperFactory();
            IWrapperEndpointNode endpoint = factory.create(tile);

            endpoint.addNeighbour(cable, side);
            endpoints.add(endpoint);

            return endpoint;
        }
    }

    public void removeEndpointNeighbour(IEndpointNode endpoint, Direction side) {
        if (endpoints.contains(endpoint)) {
            if (endpoint instanceof IWrapperEndpointNode) {
                endpoint.removeNeighbour(side);

                if (endpoint.getNeighbours().isEmpty())
                    endpoints.remove(endpoint);
            }
        }
    }

    public boolean doesEndpointExits(IEndpointNode node) {
        return endpoints.contains(node);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Collection<IEndpointNode> getAllEndpoints() {
        return endpoints;
    }

    @Nullable
    public IEndpointNode getEndpoint(TileEntity tile) {
        return endpoints.stream().filter(node -> node.getTileEntity() == tile).findAny().orElse(null);
    }
}
