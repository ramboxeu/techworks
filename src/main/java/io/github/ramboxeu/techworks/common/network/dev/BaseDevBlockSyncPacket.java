package io.github.ramboxeu.techworks.common.network.dev;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseDevBlockSyncPacket {
    protected final BlockPos pos;
    protected final EnumSet<Side> sides;
    protected final DevBlockTile.ActiveSignal signal;

    public BaseDevBlockSyncPacket(BlockPos pos, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal) {
        this.pos = pos;
        this.sides = sides;
        this.signal = signal;
    }

    protected static void encodeBase(BaseDevBlockSyncPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeEnumValue(packet.signal);
        buf.writeVarInt(packet.sides.size());
        packet.sides.forEach(buf::writeEnumValue);
    }

    protected static <T extends BaseDevBlockSyncPacket> T decodeBase(PacketBuffer buf, BasePropertiesFunction<T> consumer) {
        BlockPos pos = buf.readBlockPos();
        DevBlockTile.ActiveSignal signal = buf.readEnumValue(DevBlockTile.ActiveSignal.class);
        int size = buf.readVarInt();
        EnumSet<Side> sides = EnumSet.noneOf(Side.class);

        for (int i = 0; i < size; i++) {
            sides.add(buf.readEnumValue(Side.class));
        }

        return consumer.apply(buf, pos, sides, signal);
    }

    protected static void handleBase(BaseDevBlockSyncPacket packet, Supplier<NetworkEvent.Context> context, Consumer<DevBlockTile> consumer) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.getSender();
            TileEntity tile = sender.world.getTileEntity(packet.pos);

            if (tile instanceof DevBlockTile) {
                consumer.accept((DevBlockTile) tile);
            }
        });
        ctx.setPacketHandled(true);
    }

    protected interface BasePropertiesFunction<T extends BaseDevBlockSyncPacket> {
        T apply(PacketBuffer buf, BlockPos pos, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal);
    }

    protected static void writeFluid(Fluid fluid, PacketBuffer buf) {
        if (fluid != null) {
            buf.writeBoolean(true);
            buf.writeResourceLocation(fluid.getRegistryName());
        } else {
            buf.writeBoolean(false);
        }
    }

    protected static Fluid readFluid(PacketBuffer buf) {
        if (buf.readBoolean()) {
            return ForgeRegistries.FLUIDS.getValue(buf.readResourceLocation());
        }

        return null;
    }
}
