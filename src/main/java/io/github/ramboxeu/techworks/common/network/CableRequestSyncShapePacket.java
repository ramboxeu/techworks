package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.tile.cable.AbstractCableTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CableRequestSyncShapePacket {
    private final BlockPos pos;
    private final DimensionType dimensionType;

    public CableRequestSyncShapePacket(BlockPos pos, DimensionType dimensionType) {
        this.pos = pos;
        this.dimensionType = dimensionType;
    }

    public static void encode(CableRequestSyncShapePacket packet, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("x", packet.pos.getX());
        nbt.putInt("y", packet.pos.getY());
        nbt.putInt("z", packet.pos.getZ());
        nbt.putInt("dim", packet.dimensionType.getId());

        buffer.writeCompoundTag(nbt);
    }

    public static CableRequestSyncShapePacket decode(PacketBuffer buffer) {
        CompoundNBT nbt = buffer.readCompoundTag();

        BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        DimensionType type = DimensionType.getById(nbt.getInt("dim"));

        return new CableRequestSyncShapePacket(pos, type);
    }

    public static class Handler {
        public static void handle(final CableRequestSyncShapePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
                World world = DimensionManager.getWorld(server, packet.dimensionType, false, false);
                if (world != null && world.isAreaLoaded(packet.pos, 1)) {
                    TileEntity te = world.getTileEntity(packet.pos);
                    if (te instanceof AbstractCableTile) {
                        TechworkPacketHandler.sendCableSyncPacket(world.getChunkAt(packet.pos), new CableSyncShapePacket(NBTUtils.writeCableConnections(((AbstractCableTile) te).getConnections()), packet.pos));
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
