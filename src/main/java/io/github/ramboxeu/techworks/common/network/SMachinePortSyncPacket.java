package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SMachinePortSyncPacket {
    private final BlockPos tilePos;
    private final int index;
    private final int type;
    private final int mode;

    public SMachinePortSyncPacket(BlockPos tilePos, int index, int type, int mode) {
        this.tilePos = tilePos;
        this.index = index;
        this.type = type;
        this.mode = mode;
    }

    public static void encode(SMachinePortSyncPacket packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.tilePos);
        buffer.writeByte((byte)packet.index);
        buffer.writeByte((byte)packet.type);
        buffer.writeByte((byte)packet.mode);
    }

    public static SMachinePortSyncPacket decode(PacketBuffer buffer) {
        return new SMachinePortSyncPacket(
                buffer.readBlockPos(),
                buffer.readByte(),
                buffer.readByte(),
                buffer.readByte()
        );
    }

    public static void handle(SMachinePortSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            BlockPos tilePos = packet.tilePos;
            World world = Minecraft.getInstance().world;
            if (world.isAreaLoaded(tilePos, 1)) {
                TileEntity te = world.getTileEntity(tilePos);
                if (te instanceof BaseMachineTile) {
                    // FIXME: 10/10/2020
//                    BaseMachineTile machineTile = (BaseMachineTile) te;
//                    machineTile.getMachineIO().configurePort(packet.index, MachinePort.Type.values()[packet.type], MachinePort.Mode.values()[packet.mode]);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
