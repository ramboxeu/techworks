package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.EnergyStorageTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergyStorageSyncPacket {
    private final BlockPos pos;
    private final int energy;

    public EnergyStorageSyncPacket(BlockPos pos, int energy) {
        this.pos = pos;
        this.energy = energy;
    }

    public static void encode(EnergyStorageSyncPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeVarInt(packet.energy);
    }

    public static EnergyStorageSyncPacket decode(PacketBuffer buf) {
        return new EnergyStorageSyncPacket(buf.readBlockPos(), buf.readVarInt());
    }

    public static void handle(EnergyStorageSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            TileEntity tile = Minecraft.getInstance().world.getTileEntity(packet.pos);

            if (tile instanceof EnergyStorageTile) {
                ((EnergyStorageTile) tile).setContents(packet.energy);
            }
        });
        context.get().setPacketHandled(true);
    }
}
