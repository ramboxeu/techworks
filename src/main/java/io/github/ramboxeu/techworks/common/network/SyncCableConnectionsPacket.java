package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.BaseCableTile;
import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnections;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCableConnectionsPacket {
    private final BlockPos pos;
    private final CableConnections connections;

    public SyncCableConnectionsPacket(BlockPos pos, CableConnections connections) {
        this.pos = pos;
        this.connections = connections;
    }

    public static void encode(SyncCableConnectionsPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        packet.connections.serializePacket(buf);
    }

    public static SyncCableConnectionsPacket decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        CableConnections connections = new CableConnections();

        connections.deserializePacket(buf);
        return new SyncCableConnectionsPacket(pos, connections);
    }

    public static void handle(SyncCableConnectionsPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            TileEntity tile = world.getTileEntity(packet.pos);

            if (tile instanceof BaseCableTile) {
                ((BaseCableTile) tile).setConnections(packet.connections);
            }
        });
        context.get().setPacketHandled(true);
    }
}
