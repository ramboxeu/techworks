package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.debug.DebugInfoBuilder;
import io.github.ramboxeu.techworks.common.debug.IDebuggable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DebugRequestPacket {
    private final BlockPos pos;
    private final DimensionType dimensionType;

    public DebugRequestPacket(BlockPos pos, DimensionType dimensionType) {
        this.pos = pos;
        this.dimensionType = dimensionType;
    }

    public static void encode(DebugRequestPacket packet, PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("x", packet.pos.getX());
        nbt.putInt("y", packet.pos.getY());
        nbt.putInt("z", packet.pos.getZ());
        nbt.putInt("dim", packet.dimensionType.getId());

        buffer.writeCompoundTag(nbt);
    }

    public static DebugRequestPacket decode(PacketBuffer buffer) {
        CompoundNBT nbt = buffer.readCompoundTag();

        BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        DimensionType type = DimensionType.getById(nbt.getInt("dim"));

        return new DebugRequestPacket(pos, type);
    }

    public static class Handler {
        public static void handle(final DebugRequestPacket packet, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
                World world = DimensionManager.getWorld(server, packet.dimensionType, false, false);
                if (world != null) {
                    BlockState state = world.getBlockState(packet.pos);
                    TileEntity te = world.getTileEntity(packet.pos);

                    DebugInfoBuilder builder = new DebugInfoBuilder();

                    if (state.getBlock() instanceof IDebuggable) {
                        ((IDebuggable) state.getBlock()).addDebugInfo(builder);
                    }

                    if (state.hasTileEntity() && te instanceof IDebuggable) {
                        ((IDebuggable) te).addDebugInfo(builder);
                    }

//                    Techworks.LOGGER.debug("Debugging " + builder.getTitle() + ":");
//                    for (DebugInfoBuilder.Section section : builder.getSections()) {
//                        Techworks.LOGGER.debug(" - " + section.getName());
//                        for (String line : section.getLines()) {
//                            Techworks.LOGGER.debug("  "+ line);
//                        }
//                    }
                    TechworkPacketHandler.sendDebugResponsePacket(context.get().getSender(), new DebugResponsePacket(builder.getSections()));
                }
            });
            context.get().setPacketHandled(true);
        }
    }
}
