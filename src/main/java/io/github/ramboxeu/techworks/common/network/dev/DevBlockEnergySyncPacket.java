package io.github.ramboxeu.techworks.common.network.dev;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.EnumSet;
import java.util.function.Supplier;

public class DevBlockEnergySyncPacket extends BaseDevBlockSyncPacket {
    private final int energy;

    public DevBlockEnergySyncPacket(BlockPos pos, int energy, DevBlockTile.ActiveSignal signal, EnumSet<Side> sides) {
        super(pos, sides, signal);
        this.energy = energy;
    }

    public static void encode(DevBlockEnergySyncPacket packet, PacketBuffer buf) {
        encodeBase(packet, buf);
        buf.writeVarInt(packet.energy);
    }

    public static DevBlockEnergySyncPacket decode(PacketBuffer buffer) {
        return decodeBase(buffer, (buf, pos, sides, signal) -> {
            int energy = buf.readVarInt();
            return new DevBlockEnergySyncPacket(pos, energy, signal, sides);
        });
    }

    public static void handle(DevBlockEnergySyncPacket packet, Supplier<NetworkEvent.Context> context) {
        handleBase(packet, context, tile -> tile.configureEnergy(packet.energy, packet.sides, packet.signal));
    }
}
