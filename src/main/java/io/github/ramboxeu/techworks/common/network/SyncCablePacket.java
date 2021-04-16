package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.ItemTransporterTile;
import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncCablePacket {
    private final ICablePacket packet;
    private final BlockPos pos;
    private final boolean arrived;
    private final int index;

    public SyncCablePacket(ICablePacket packet, BlockPos pos, int index, boolean arrived) {
        this.packet = packet;
        this.pos = pos;
        this.arrived = arrived;
        this.index = index;
    }


    public static void encode(SyncCablePacket packet, PacketBuffer buffer) {
        buffer.writeBoolean(packet.arrived);
        buffer.writeVarInt(packet.index);
        buffer.writeBlockPos(packet.pos);

        if (packet.arrived) {
            buffer.writeEnumValue(packet.packet.getType());
            packet.packet.writeToPacket(buffer);
        }
    }

    public static SyncCablePacket decode(PacketBuffer buffer) {
        boolean arrived = buffer.readBoolean();
        int index = buffer.readVarInt();
        BlockPos pos = buffer.readBlockPos();

        if (arrived) {
            PacketType type = buffer.readEnumValue(PacketType.class);
            ICablePacket packet = type.decode(buffer);
            return new SyncCablePacket(packet, pos, index, arrived);
        }

        return new SyncCablePacket(null, pos, index, arrived);
    }

    public static void handle(SyncCablePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            TileEntity tile = world.getTileEntity(packet.pos);

            if (tile instanceof ItemTransporterTile) {
                ItemTransporterTile cable = (ItemTransporterTile) tile;

                if (packet.arrived) {
                    cable.onPacketArrived(packet.packet);
                } else {
                    cable.getPackets().remove(packet.index);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
