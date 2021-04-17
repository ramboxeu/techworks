package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.client.model.cable.CableModel;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.util.HandlerStorage;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.RedstoneMode;
import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnections;
import io.github.ramboxeu.techworks.common.util.cable.connection.ConnectionMode;
import io.github.ramboxeu.techworks.common.util.cable.connection.ConnectionStatus;
import io.github.ramboxeu.techworks.common.util.cable.network.*;
import io.github.ramboxeu.techworks.common.util.model.CuboidGroup;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class BaseCableTile extends TileEntity implements ITickableTileEntity, IWrenchable, INode {

    public static final ModelProperty<CableConnections> CONNECTIONS = new ModelProperty<>();

    private final ModelDataMap modelData = new ModelDataMap.Builder().withProperty(CONNECTIONS).build();
    private final Map<Direction, INode> neighbours = new EnumMap<>(Direction.class);
    private final NetworkType networkType;
    private final RedstoneMode redstoneMode = RedstoneMode.LOW;

    protected CableConnections connections = new CableConnections();
    private ICableNetworkHolder network;
    private boolean firstTicked = false;
    private int cooldownTimer = 0;

    protected UUID networkId;

    public BaseCableTile(TileEntityType<?> type, NetworkType networkType) {
        super(type);
        this.networkType = networkType;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Connections", connections.serializeNBT());
        tag.putInt("CooldownTimer", cooldownTimer);

        if (network != null)
            tag.putUniqueId("NetworkId", network.getId());

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        connections.deserializeNBT(tag.getCompound("Connections"));
        cooldownTimer = tag.getInt("CooldownTimer");
        networkId = NBTUtils.getNetworkId(tag);

        super.read(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();

        tag.put("Connections", connections.serializeNBT());

        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        connections.deserializeNBT(tag.getCompound("Connections"));
        refreshData();
    }

    @NotNull
    @Override
    public IModelData getModelData() {
        return modelData;
    }

    @Override
    public void remove() {
        if (!world.isRemote) {
            List<INode> splitPoints = new ArrayList<>(6);

            for (Map.Entry<Direction, INode> entry : neighbours.entrySet()) {
                INode node = entry.getValue();

                if (node instanceof IEndpointNode) {
                    network.getNetwork().removeEndpointNeighbour((IEndpointNode) node, entry.getKey());
                } else {
                    node.removeNeighbour(entry.getKey().getOpposite());
                    splitPoints.add(node);
                }
            }

            CableNetworkManager.get(world).splitNetwork(network.getNetwork(), splitPoints);
        }

        super.remove();
    }

    public void firstTick() {
        EnumMap<Direction, TileEntity> tiles = new EnumMap<>(Direction.class);

        for (Direction side : Direction.values()) {
            TileEntity tile = world.getTileEntity(pos.offset(side));

            if (tile != null)
                tiles.put(side, tile);
        }

        if (!world.isRemote) {
            for (TileEntity tile : tiles.values()) {
                if (tile instanceof BaseCableTile) {
                    BaseCableTile cable = (BaseCableTile) tile;

                    if (cable.networkType == networkType) {
                        ICableNetworkHolder other = cable.network;

                        if (this.network == null) {
                            setNetwork(other);
                        } else {
                            if (!network.equals(other) && other != null) {
                                CableNetworkManager.get(world).mergeNetworks(network.getNetwork(), other.getNetwork());
                            }
                        }
                    }
                }
            }
        }

        if (network == null && !world.isRemote)
            setNetwork(CableNetworkManager.get(world).getNetwork(networkType, networkId));

        for (Map.Entry<Direction, TileEntity> entry : tiles.entrySet()) {
            Direction side = entry.getKey();
            TileEntity tile = entry.getValue();

            if (canConnect(side, tile))
                connect(side, tile, true);
        }

        refreshData();
    }

    @Override
    public void setHolder(ICableNetworkHolder holder) {
        setNetwork(holder);
    }

    @Override
    public void tick() {
        if (!firstTicked) {
            firstTick();
            firstTicked = true;
        }

        if (!world.isRemote) {
            if (!getHandlerStorage().isEmpty() && redstoneMode.canWork(world.isBlockPowered(pos))) {
                if (cooldownTimer == 20) {
                    extract();
                    cooldownTimer = 0;
                } else {
                    cooldownTimer += 1;
                }
            }
        }
    }

    @Override
    public boolean configure(World world, BlockPos pos, Direction face, Vector3d hitVec) {
        if (CableModel.CENTER.isVectorWithin(hitVec)) {
            if (connections.getStatus(face).isBlocked()) {
                TileEntity tile = world.getTileEntity(pos.offset(face));

                connections.setStatus(face, ConnectionStatus.DISCONNECTED);

                if (canConnect(face, tile))
                    connect(face, tile, true);
            } else {
                disconnect(face, true);
                connections.setStatus(face, ConnectionStatus.BLOCKED);
            }
        } else {
            for (Map.Entry<Direction, CuboidGroup> entry : CableModel.CUBOID_MAP.entrySet()) {
                CuboidGroup group = entry.getValue();
                Direction dir = entry.getKey();
                if (group.getConnection().isVectorWithin(hitVec) || group.getConnected().isVectorWithin(hitVec)) {
                    disconnect(dir, true);
                    connections.setStatus(dir, ConnectionStatus.BLOCKED);
                    break;
                }

                if (group.getConnector().isVectorWithin(hitVec)) {
                    connections.setMode(dir, connections.getMode(dir).next());
                    break;
                }
            }
        }

        if (world.isRemote) {
            refreshData();
        } else {
            world.notifyNeighborsOfStateChange(pos, getBlockState().getBlock());
        }

        return true;
    }

    @Override
    public Map<Direction, INode> getNeighboursMap() {
        return neighbours;
    }

    @Override
    public BlockPos getPosition() {
        return this.pos;
    }

    @Override
    public boolean isTraversable() {
        return true;
    }

    @Override
    public void removeNeighbour(Direction side) {
        neighbours.remove(side);
    }

    @Override
    public ConnectionMode getConnectionMode(Direction side) {
        return connections.getMode(side);
    }

    @Override
    public void onPacketArrived(ICablePacket packet) {}

    @Override
    public void onPacketDeparted(ICablePacket packet) {}

    protected void setNetwork(ICableNetworkHolder network) {
        this.network = network;
    }

    public void updateConnections(Direction side) {
        BlockPos neighbourPos = pos.offset(side);
        TileEntity tile = world.getTileEntity(neighbourPos);

        if (tile == null || !canConnect(side, tile)) {
            disconnect(side, false);
        } else {
            connect(side, tile, false);
        }

        syncConnections();
    }

    private void refreshData() {
        modelData.setData(CONNECTIONS, connections);
        requestModelDataUpdate();

        if (world != null) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    private boolean canConnect(Direction side, TileEntity tile) {
        if (connections.getStatus(side).isBlocked())
            return false;

        if (tile instanceof BaseCableTile) {
            BaseCableTile cable = (BaseCableTile) tile;

            if (cable.networkType == networkType)
                return !cable.getConnections().getStatus(side.getOpposite()).isBlocked();

            return false;
        }

        return isTileValid(side, tile);
    }

    private void connect(Direction side, TileEntity tile, boolean merge) {
        if (tile instanceof BaseCableTile) {
            BaseCableTile cable = (BaseCableTile) tile;

            if (cable.networkType == networkType) {
                connections.setConnection(side, ConnectionMode.CONNECTION, true);
                addCableNeighbour(side, cable, merge);
            }
        } else {
            connections.setConnection(side, connections.getMode(side).orBoth(), true);
            getHandlerStorage().store(side, tile);
            addEndpointNeighbour(side, tile);
        }
    }

    private void addCableNeighbour(Direction side, BaseCableTile tile, boolean merge) {
        if (!world.isRemote) {
            neighbours.put(side, tile);

            if (tile.network != null && !network.equals(tile.network) && merge)
                CableNetworkManager.get(world).mergeNetworks(network.getNetwork(), tile.network.getNetwork());
        }
    }

    private void addEndpointNeighbour(Direction side, TileEntity tile) {
        if (!world.isRemote) {
            IEndpointNode endpoint = network.getNetwork().getOrCreateEndpoint(side, tile, this);
            neighbours.put(side, endpoint);
        }
    }

    private void disconnect(Direction side, boolean split) {
        connections.setStatus(side, false);
        getHandlerStorage().remove(side);
        removeNeighbour(side, split);
    }

    private void removeNeighbour(Direction side, boolean split) {
        if (!world.isRemote) {
            INode neighbour = neighbours.remove(side);

            if (neighbour instanceof IEndpointNode) {
                network.getNetwork().removeEndpointNeighbour((IEndpointNode) neighbour, side);
            } else {
                if (neighbour != null) {
                    if (split && neighbours.size() > 0)
                        CableNetworkManager.get(world).splitNetwork(network.getNetwork(), Arrays.asList(neighbour, this));
                }
            }
        }
    }

    private void syncConnections() {
        if (!world.isRemote)
            TechworksPacketHandler.syncCableConnections(world.getChunkAt(pos), pos, connections);
    }

    public CableConnections getConnections() {
        return connections;
    }

    public void setConnections(CableConnections connections) {
        this.connections = connections;
        refreshData();
    }

    protected abstract boolean isTileValid(Direction side, TileEntity tile);
    protected abstract HandlerStorage<?> getHandlerStorage();
    protected abstract void extract();

    // For debug only
    @Deprecated
    public BaseCableNetwork getNetwork() {
        return network.getNetwork();
    }
}
