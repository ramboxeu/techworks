package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.OreMinerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncOreMinerStatusPacket {
    private final BlockPos pos;
    private final OreMinerTile.Status status;

    public SyncOreMinerStatusPacket(BlockPos pos, OreMinerTile.Status status) {
        this.pos = pos;
        this.status = status;
    }

    public static void encode(SyncOreMinerStatusPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeEnumValue(packet.status);
    }

    public static SyncOreMinerStatusPacket decode(PacketBuffer buf) {
        return new SyncOreMinerStatusPacket(buf.readBlockPos(), buf.readEnumValue(OreMinerTile.Status.class));
    }

    public static void handle(SyncOreMinerStatusPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            TileEntity te = world.getTileEntity(packet.pos);

            if (te instanceof OreMinerTile) {
                ((OreMinerTile) te).setStatus(packet.status);
            }
        });
        context.get().setPacketHandled(true);
    }
}
