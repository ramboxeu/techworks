package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DevBlockLogSyncPacket {
    private final BlockPos pos;
    private final String log;

    public DevBlockLogSyncPacket(BlockPos pos, String log) {
        this.pos = pos;
        this.log = log;
    }

    public static void encode(DevBlockLogSyncPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeString(packet.log);
    }

    public static DevBlockLogSyncPacket decode(PacketBuffer buf) {
        return new DevBlockLogSyncPacket(buf.readBlockPos(), buf.readString());
    }

    public static void handle(DevBlockLogSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            TileEntity tile = mc.world.getTileEntity(packet.pos);

            if (tile instanceof DevBlockTile) {
                ((DevBlockTile) tile).appendLog(packet.log);
            }
        });
        context.get().setPacketHandled(true);
    }
}
