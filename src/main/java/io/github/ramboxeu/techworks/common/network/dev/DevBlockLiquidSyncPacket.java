package io.github.ramboxeu.techworks.common.network.dev;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumSet;
import java.util.function.Supplier;

public class DevBlockLiquidSyncPacket extends BaseDevBlockSyncPacket {
    private final int liquidPerTick;
    private final Fluid liquid;

    public DevBlockLiquidSyncPacket(BlockPos pos, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal, int liquidPerTick, Fluid liquid) {
        super(pos, sides, signal);
        this.liquidPerTick = liquidPerTick;
        this.liquid = liquid;
    }

    public static void encode(DevBlockLiquidSyncPacket packet, PacketBuffer buf) {
        encodeBase(packet, buf);
        buf.writeVarInt(packet.liquidPerTick);
        writeFluid(packet.liquid, buf);
    }

    public static DevBlockLiquidSyncPacket decode(PacketBuffer buffer) {
        return decodeBase(buffer, (buf, pos, sides, signal) -> {
            int liquidPerTick = buf.readVarInt();
            Fluid liquid = readFluid(buf);
            return new DevBlockLiquidSyncPacket(pos, sides, signal, liquidPerTick, liquid);
        });
    }

    public static void handle(DevBlockLiquidSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        handleBase(packet, supplier, tile -> tile.configureLiquid(packet.liquidPerTick, packet.liquid, packet.sides, packet.signal));
    }
}
