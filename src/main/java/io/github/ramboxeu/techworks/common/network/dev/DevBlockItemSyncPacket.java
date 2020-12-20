package io.github.ramboxeu.techworks.common.network.dev;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class DevBlockItemSyncPacket extends BaseDevBlockSyncPacket {
    private final List<ItemStack> inv;

    public DevBlockItemSyncPacket(BlockPos pos, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal, List<ItemStack> inv) {
        super(pos, sides, signal);
        this.inv = inv;
    }

    public static void encode(DevBlockItemSyncPacket packet, PacketBuffer buf) {
        encodeBase(packet, buf);
        buf.writeVarInt(packet.inv.size());
        for (ItemStack stack : packet.inv) {
            buf.writeItemStack(stack, true);
        }
    }

    public static DevBlockItemSyncPacket decode(PacketBuffer buffer) {
        return decodeBase(buffer, (buf, pos, sides, signal) -> {
            int size = buf.readVarInt();
            List<ItemStack> inv = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                inv.add(i, buf.readItemStack());
            }
            return new DevBlockItemSyncPacket(pos, sides, signal, inv);
        });
    }

    public static void handle(DevBlockItemSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        handleBase(packet, context, tile -> tile.configureItem(packet.inv, packet.sides, packet.signal));
    }
}
