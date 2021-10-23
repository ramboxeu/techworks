package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.RedstoneMode;
import io.github.ramboxeu.techworks.common.util.StandbyMode;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MachineWorkStateSyncPacket {
    private final BlockPos pos;
    private final RedstoneMode redstoneMode;
    private final StandbyMode standbyMode;

    public MachineWorkStateSyncPacket(BlockPos pos, RedstoneMode redstoneMode, StandbyMode standbyMode) {
        this.pos = pos;
        this.redstoneMode = redstoneMode;
        this.standbyMode = standbyMode;
    }

    public static void encode(MachineWorkStateSyncPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        writeEnum(buf, packet.redstoneMode);
        writeEnum(buf, packet.standbyMode);
    }

    public static MachineWorkStateSyncPacket decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        RedstoneMode redstoneMode = readEnum(buf, RedstoneMode.class);
        StandbyMode standbyMode = readEnum(buf, StandbyMode.class);
        return new MachineWorkStateSyncPacket(pos, redstoneMode, standbyMode);
    }

    private static void writeEnum(PacketBuffer buf, Enum<?> val) {
        buf.writeVarInt(val != null ? val.ordinal() : -1);
    }

    private static <T extends Enum<T>> T readEnum(PacketBuffer buf, Class<T> clazz) {
        int index = buf.readVarInt();
        return index > -1 ? clazz.getEnumConstants()[index] : null;
    }

    public static void handle(MachineWorkStateSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            World world = context.get().getSender().world;
            TileEntity tile = world.getTileEntity(packet.pos);

            if (tile instanceof BaseMachineTile) {
                ((BaseMachineTile) tile).setRedstoneMode(packet.redstoneMode);
                ((BaseMachineTile) tile).setStandbyMode(packet.standbyMode);
            }
        });
        context.get().setPacketHandled(true);
    }
}
