package io.github.ramboxeu.techworks.common.network.dev;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumSet;
import java.util.function.Supplier;

public class DevBlockGasSyncPacket extends BaseDevBlockSyncPacket {
    private final int gasPerTick;
    private final Fluid gas;

    public DevBlockGasSyncPacket(BlockPos pos, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal, int gasPerTick, Fluid gas) {
        super(pos, sides, signal);
        this.gasPerTick = gasPerTick;
        this.gas = gas;
    }

    public static void encode(DevBlockGasSyncPacket packet, PacketBuffer buf) {
        encodeBase(packet, buf);
        buf.writeVarInt(packet.gasPerTick);
        writeFluid(packet.gas, buf);
    }

    public static DevBlockGasSyncPacket decode(PacketBuffer buffer) {
        return decodeBase(buffer, (buf, pos, sides, signal) -> {
            int liquidPerTick = buf.readVarInt();
            Fluid liquid = readFluid(buf);
            return new DevBlockGasSyncPacket(pos, sides, signal, liquidPerTick, liquid);
        });
    }

    public static void handle(DevBlockGasSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        handleBase(packet, supplier, tile -> tile.configureGas(packet.gasPerTick, packet.gas, packet.sides, packet.signal));
    }
}
