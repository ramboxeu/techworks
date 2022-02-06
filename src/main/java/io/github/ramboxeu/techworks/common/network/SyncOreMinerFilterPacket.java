package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.OreMinerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncOreMinerFilterPacket {
    private final Type type;
    private final BlockPos pos;
    private final ResourceLocation id;

    public SyncOreMinerFilterPacket(Type type, BlockPos pos, ResourceLocation id) {
        this.type = type;
        this.pos = pos;
        this.id = id;
    }

    public static void encode(SyncOreMinerFilterPacket packet, PacketBuffer buf) {
        buf.writeEnumValue(packet.type);
        buf.writeBlockPos(packet.pos);

        if (packet.type != Type.CLEAR)
            buf.writeResourceLocation(packet.id);
    }

    public static SyncOreMinerFilterPacket decode(PacketBuffer buf) {
        Type type = buf.readEnumValue(Type.class);
        BlockPos pos = buf.readBlockPos();

        ResourceLocation id;
        if (type != Type.CLEAR) id = buf.readResourceLocation();
        else id = null;

        return new SyncOreMinerFilterPacket(type, pos, id);
    }

    public static void handle(SyncOreMinerFilterPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            World world = player != null ? player.world : Minecraft.getInstance().world;
            TileEntity te = world.getTileEntity(packet.pos);

            if (te instanceof OreMinerTile) {
                OreMinerTile.FilterType type;

                if (packet.type == Type.BLOCK) type = OreMinerTile.FilterType.BLOCK;
                else if (packet.type == Type.TAG) type = OreMinerTile.FilterType.TAG;
                else type = null;

                ((OreMinerTile) te).syncFilter(type, packet.id);
            }
        });
        context.get().setPacketHandled(true);
    }

    public enum Type {
        BLOCK,
        TAG,
        CLEAR
    }
}
