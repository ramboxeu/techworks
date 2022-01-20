package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.common.tile.GasTankTile;
import io.github.ramboxeu.techworks.common.tile.LiquidTankTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidStorageSyncPacket {
    private final BlockPos pos;
    private final FluidStack fluid;

    public FluidStorageSyncPacket(BlockPos pos, FluidStack fluid) {
        this.pos = pos;
        this.fluid = fluid;
    }

    public static void encode(FluidStorageSyncPacket packet, PacketBuffer buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeFluidStack(packet.fluid);
    }

    public static FluidStorageSyncPacket decode(PacketBuffer buf) {
        return new FluidStorageSyncPacket(buf.readBlockPos(), buf.readFluidStack());
    }

    public static void handle(FluidStorageSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;

            if (world != null) {
                TileEntity tile = world.getTileEntity(packet.pos);

                if (tile instanceof LiquidTankTile) {
                    ((LiquidTankTile) tile).setTankContents(packet.fluid);
                }

                if (tile instanceof GasTankTile) {
                    ((GasTankTile) tile).setTankContents(packet.fluid);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
