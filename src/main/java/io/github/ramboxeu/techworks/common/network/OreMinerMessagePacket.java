package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.OreMinerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OreMinerMessagePacket {
    private final BlockPos pos;
    private final Type type;

    public OreMinerMessagePacket(BlockPos pos, Type type) {
        this.pos = pos;
        this.type = type;
    }

    public static void encode(OreMinerMessagePacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeEnumValue(packet.type);
    }

    public static OreMinerMessagePacket decode(PacketBuffer buf) {
        return new OreMinerMessagePacket(buf.readBlockPos(), buf.readEnumValue(Type.class));
    }

    public static void handle(OreMinerMessagePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity sender = context.get().getSender();
            World world = sender != null ? sender.world : Minecraft.getInstance().world;
            TileEntity tile = world.getTileEntity(packet.pos);

            if (tile instanceof OreMinerTile) {
                OreMinerTile miner = (OreMinerTile) tile;
                if (packet.type == Type.RESCAN) {
                    miner.rescan();
                } else {
                    miner.setWorking(packet.type == Type.WORKING_ON);
                }
            }
        });
        context.get().setPacketHandled(true);
    }

    public enum Type {
        RESCAN,
        WORKING_ON,
        WORKING_OFF
    }
}
