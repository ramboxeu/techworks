package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MetalPressSyncPacket {
    private final BlockPos pos;
    private final MetalPressTile.Mode mode;

    public MetalPressSyncPacket(BlockPos pos, MetalPressTile.Mode mode) {
        this.pos = pos;
        this.mode = mode;
    }

    public static void encode(MetalPressSyncPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeEnumValue(packet.mode);
    }

    public static MetalPressSyncPacket decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        MetalPressTile.Mode mode = buf.readEnumValue(MetalPressTile.Mode.class);

        return new MetalPressSyncPacket(pos, mode);
    }

    public static void handle(MetalPressSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            World world = context.get().getSender().world;
            TileEntity tile = world.getTileEntity(packet.pos);

            if (tile instanceof MetalPressTile)
                ((MetalPressTile) tile).setMode(packet.mode);

        });
        context.get().setPacketHandled(true);
    }
}
