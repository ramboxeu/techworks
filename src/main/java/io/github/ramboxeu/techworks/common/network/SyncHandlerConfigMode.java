package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncHandlerConfigMode {
    private final BlockPos pos;
    private final int id;
    private final Side side;
    private final StorageMode mode;
    private final InputType type;

    public SyncHandlerConfigMode(BlockPos pos, int id, Side side, StorageMode mode, InputType type) {
        this.pos = pos;
        this.id = id;
        this.side = side;
        this.mode = mode;
        this.type = type;
    }

    public static void encode(SyncHandlerConfigMode msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeVarInt(msg.id);
        buffer.writeEnumValue(msg.side);
        buffer.writeEnumValue(msg.mode);
        buffer.writeEnumValue(msg.type);
    }

    public static SyncHandlerConfigMode decode(PacketBuffer buffer) {
        return new SyncHandlerConfigMode(buffer.readBlockPos(),
                buffer.readVarInt(),
                buffer.readEnumValue(Side.class),
                buffer.readEnumValue(StorageMode.class),
                buffer.readEnumValue(InputType.class));
    }

    public static void handle(SyncHandlerConfigMode msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity entity = context.get().getSender();
            if (entity != null) {
                TileEntity tile = entity.getServerWorld().getTileEntity(msg.pos);

                if (tile instanceof BaseMachineTile) {
                    ((BaseMachineTile) tile).getMachineIO().setHandlerConfigMode(msg.id, msg.side, msg.mode, msg.type);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
