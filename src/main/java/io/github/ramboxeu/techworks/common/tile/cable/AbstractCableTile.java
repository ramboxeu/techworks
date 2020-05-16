package io.github.ramboxeu.techworks.common.tile.cable;

import io.github.ramboxeu.techworks.common.debug.DebugInfoBuilder;
import io.github.ramboxeu.techworks.common.debug.IDebuggable;
import io.github.ramboxeu.techworks.common.model.TechworksModelData;
import io.github.ramboxeu.techworks.common.network.CableRequestSyncShapePacket;
import io.github.ramboxeu.techworks.common.network.CableSyncShapePacket;
import io.github.ramboxeu.techworks.common.network.TechworkPacketHandler;
import io.github.ramboxeu.techworks.common.util.CableConnections;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCableTile<THandler> extends TileEntity implements ITickableTileEntity, IDebuggable {
    private CableConnections connections;
    private CableConnections disabled;
    protected LazyOptional<THandler> handler;
    private Capability<THandler> capability;
    private boolean synced = false;
    private List<Direction> inputs = new ArrayList<>();

    public AbstractCableTile(TileEntityType<?> tileEntityType, Capability<THandler> capability) {
        super(tileEntityType);
        connections = new CableConnections.Builder().build();
        this.disabled = new CableConnections.Builder().build();
        handler = LazyOptional.of(this::createHandler);
        this.capability = capability;
    }

    protected abstract THandler createHandler();

    protected abstract boolean transfer(THandler pipe, List<THandler> transferees);

    protected abstract THandler createCapability(Direction side, THandler handler, List<Direction> inputs);

    // This is a complete GARBAGE, too bad
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == capability && side != null) {
            if (handler.isPresent()) {
                return LazyOptional.of(() -> createCapability(side, handler.orElseGet(() -> null), inputs)).cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        handler.ifPresent(handler -> {
            ArrayList<THandler> handlers = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                Direction direction = Direction.byIndex(i);
                TileEntity te = world.getTileEntity(pos.offset(direction));
                if (te != null && !inputs.contains(direction)) {
                    te.getCapability(capability, direction.getOpposite()).ifPresent(handlers::add);
                }
            }

            if (handlers.size() > 0) {
                transfer(handler, handlers);
            }

            inputs.clear();
        });
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.connections = NBTUtils.readCableConnections(compound.getCompound("cableConnections"));
        handler.ifPresent(h -> capability.readNBT(h, Direction.NORTH, compound.getCompound("storage")));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("cableConnections", NBTUtils.writeCableConnections(this.connections));
        handler.ifPresent(h -> compound.put("storage", capability.writeNBT(h, Direction.NORTH)));
        return super.write(compound);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        if (world.isRemote && !synced) {
            synced = true;
            TechworkPacketHandler.sendRequestCableSyncPacket(new CableRequestSyncShapePacket(this.pos, this.world.dimension.getType()));
        }
        return new ModelDataMap.Builder().withInitial(TechworksModelData.PIPE_CONNECTIONS, connections).build();
    }

    public void updateConnections(boolean notifyNeighbours) {
        World world = this.getWorld();
        BlockPos pos = this.getPos();

        if (world != null && pos != BlockPos.ZERO && !world.isRemote()) {

            CableConnections.Builder builder = new CableConnections.Builder();
            for (int i = 0; i < 6; i++) {
                Direction direction = Direction.byIndex(i);
                TileEntity tileEntity = world.getTileEntity(pos.offset(direction));
                if (tileEntity != null && tileEntity.getCapability(capability, direction.getOpposite()).isPresent()) {
                    builder.setDirection(true, direction);
                } else {
                    builder.setDirection(false, direction);
                }
                if (notifyNeighbours) {
                    world.getBlockState(pos.offset(direction)).neighborChanged(world, pos.offset(direction), getBlockState().getBlock(), pos, false);
                }
            }

            this.connections = builder.build();

            TechworkPacketHandler.sendCableSyncPacket(world.getChunkAt(pos), new CableSyncShapePacket(NBTUtils.writeCableConnections(this.connections), pos));
            markDirty();
        }
    }

    public void syncConnections(CableConnections connections) {
        this.connections = connections;
        if (world != null && world.isRemote) {
            ModelDataManager.requestModelDataRefresh(this);
            Minecraft.getInstance().worldRenderer.notifyBlockUpdate(world, pos, getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    public CableConnections getConnections() {
        return connections;
    }

    @Override
    public void addDebugInfo(DebugInfoBuilder builder) {
        DebugInfoBuilder.Section connections = new DebugInfoBuilder.Section("Connections");

        this.connections.getAsMap().forEach((direction, value) -> connections.line(direction.getName() + ": " + value));
        builder.addSection(connections);

        DebugInfoBuilder.Section inputs = new DebugInfoBuilder.Section("Inputs");
        inputs.line(Arrays.toString(this.inputs.toArray(new Direction[0])));
        builder.addSection(inputs);
    }
}
