//package io.github.ramboxeu.techworks.common.util.cable.network;
//
//import io.github.ramboxeu.techworks.Techworks;
//import io.github.ramboxeu.techworks.common.tile.CableTile;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//
//import javax.annotation.Nullable;
//import java.util.*;
//import java.util.function.BiConsumer;
//
///*
// * Fluids & Energy: have a global storage (makes things instantaneous, if its a problem) (also how do we make it distribute things)
// *
// * Packet storage
// * Items - render in one tile (how?)
// * Fluids & Energy - render in all tiles
// * Fluids & Energy - using standard pathfinding find endpoint, calc time
// * Fluids - lock on one transported fluid
// */
//public class CableNetwork {
//    // FIXME: Can't really do ROUND_ROBIN using this setup
//    private final Set<IEndpointNode> endpoints = new HashSet<>();
//    private final NetworkType type;
//    private final UUID id;
//
//    CableNetwork(NetworkType type, UUID id) {
//        this.type = type;
//        this.id = id;
//    }
//
//    public NetworkType getType() {
//        return type;
//    }
//
//    public Optional<IEndpointNode> getEndpoint(TileEntity tile) {
//        return endpoints.stream().filter(node -> node.getTileEntity() == tile).findAny();
//    }
//
//    public Collection<IEndpointNode> getAllEndpoints() {
//        return endpoints;
//    }
//
//    @Nullable @Deprecated
//    public IEndpointNode getEndpoint() {
//        return endpoints.stream().filter(node -> !node.isFull()).findFirst().orElse(null);
//    }
//
//    public IEndpointNode getTransferTarget(TransferType type, Collection<INode> ignoredNodes) {
//        switch (type) {
//            case NORMAL:
//                return getNormalTarget(ignoredNodes);
//            case ROUND_ROBIN:
//                return getRoundRobinTarget(ignoredNodes);
//        }
//
//        return null;
//    }
//
//    // TODO: Implement
//    private IEndpointNode getRoundRobinTarget(Collection<INode> ignoredNodes) {
//        Techworks.LOGGER.debug("Round robin not supported yet! Returning normal");
//        return getNormalTarget(ignoredNodes);
//    }
//
//    private IEndpointNode getNormalTarget(Collection<INode> ignoredNodes) {
//        return endpoints.stream()
//                .filter(node -> !ignoredNodes.contains(node) && !node.isFull())
//                .findAny()
//                .orElse(null);
//    }
//
//    @Deprecated // TODO: Remove
//    public void neighbour(Direction side, @Nullable TileEntity tile, INode cable, BiConsumer<Direction, INode> consumer) {
//        if (tile != null) {
//            if (tile instanceof INode) {
//                if (tile instanceof IEndpointNode)
//                    endpoints.add((IEndpointNode) tile);
//
//                consumer.accept(side, (INode) tile);
//            } else {
//                Optional<IEndpointNode> optional = endpoints.stream().filter(n -> n.getTileEntity() == tile).findAny();
//
//                if (optional.isPresent()) {
//                    IEndpointNode endpoint = optional.get();
//
//                    if (endpoint instanceof IWrapperEndpointNode)
//                        ((IWrapperEndpointNode) endpoint).addNeighbour(cable, side);
//
//                    consumer.accept(side, endpoint);
//                } else {
//                    IWrapperEndpointNode.Factory factory = type.getWrapperFactory();
//                    IWrapperEndpointNode node = factory.create(tile);
//
//                    node.addNeighbour(cable, side);
//                    endpoints.add(node);
//                    consumer.accept(side, node);
//                }
//            }
//        }
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//
//        if (obj instanceof CableNetwork)
//            return ((CableNetwork) obj).id.equals(id);
//
//        return false;
//    }
//
//    public IEndpointNode getOrCreateEndpoint(Direction side, TileEntity tile, CableTile cable) {
//        Optional<IEndpointNode> optional = endpoints.stream().filter(n -> n.getTileEntity() == tile).findAny();
//
//        if (optional.isPresent()) {
//            IEndpointNode endpoint = optional.get();
//
//            if (endpoint instanceof IWrapperEndpointNode)
//                ((IWrapperEndpointNode) endpoint).addNeighbour(cable, side);
//
//            return endpoint;
//        } else {
//            IWrapperEndpointNode.Factory factory = type.getWrapperFactory();
//            IWrapperEndpointNode endpoint = factory.create(tile);
//
//            endpoint.addNeighbour(cable, side);
//            endpoints.add(endpoint);
//
//            return endpoint;
//        }
//    }
//
//    public void removeEndpointNeighbour(IEndpointNode endpoint, Direction side) {
//        if (endpoints.contains(endpoint)) {
//            if (endpoint instanceof IWrapperEndpointNode) {
//                ((IWrapperEndpointNode) endpoint).removeNeighbour(side);
//
//                if (endpoint.getNeighbours().isEmpty())
//                    endpoints.remove(endpoint);
//            }
//        }
//    }
//}
