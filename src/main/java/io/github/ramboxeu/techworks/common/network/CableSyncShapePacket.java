package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.cable.AbstractCableTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CableSyncShapePacket {
    private final CompoundNBT nbt;
    private final BlockPos pos;

    public CableSyncShapePacket(CompoundNBT nbt, BlockPos pos) {
        this.nbt = nbt;
        this.pos = pos;
    }

    public static void encode(CableSyncShapePacket packet, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("x", packet.pos.getX());
        nbt.putInt("y", packet.pos.getY());
        nbt.putInt("z", packet.pos.getZ());
        nbt.put("connections", packet.nbt);

        buffer.writeCompoundTag(nbt);
    }

    public static CableSyncShapePacket decode(PacketBuffer buffer) {
        CompoundNBT nbt = buffer.readCompoundTag();

        BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        CompoundNBT connections = nbt.getCompound("connections");

        return new CableSyncShapePacket(connections, pos);
    }

    public static class Handler {
        public static void handle(final CableSyncShapePacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                TileEntity te = Minecraft.getInstance().world.getTileEntity(packet.pos);
                if (te instanceof AbstractCableTile) {
                    ((AbstractCableTile) te).syncConnections(NBTUtils.readCableConnections(packet.nbt));
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
