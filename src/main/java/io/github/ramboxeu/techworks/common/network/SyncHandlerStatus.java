package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.AutoMode;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncHandlerStatus {
    private final int id;
    private final Side side;
    private final InputType type;
    private final StorageMode mode;
    private final BlockPos pos;
    private final AutoMode autoMode;
    private final boolean enabled;

    public SyncHandlerStatus(int id, Side side, InputType type, StorageMode mode, BlockPos pos, AutoMode autoMode, boolean enabled) {
        this.id = id;
        this.side = side;
        this.type = type;
        this.mode = mode;
        this.pos = pos;
        this.autoMode = autoMode;
        this.enabled = enabled;
    }

    public static SyncHandlerStatus decode(PacketBuffer buffer) {
        return new SyncHandlerStatus(buffer.readVarInt(),
                buffer.readEnumValue(Side.class),
                buffer.readEnumValue(InputType.class),
                buffer.readEnumValue(StorageMode.class),
                buffer.readBlockPos(),
                buffer.readEnumValue(AutoMode.class),
                buffer.readBoolean()
        );
    }

    public static void encode(SyncHandlerStatus packet, PacketBuffer buffer) {
        buffer.writeVarInt(packet.id);
        buffer.writeEnumValue(packet.side);
        buffer.writeEnumValue(packet.type);
        buffer.writeEnumValue(packet.mode);
        buffer.writeBlockPos(packet.pos);
        buffer.writeEnumValue(packet.autoMode);
        buffer.writeBoolean(packet.enabled);
    }

    public static void handle(SyncHandlerStatus packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity entity = context.get().getSender();
            if (entity != null) {
                TileEntity tile = entity.getServerWorld().getTileEntity(packet.pos);

                if (tile instanceof BaseMachineTile) {
                    ((BaseMachineTile) tile).getMachineIO().setConfigStatus(packet.id, packet.side, packet.type, packet.mode, packet.autoMode, packet.enabled);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
