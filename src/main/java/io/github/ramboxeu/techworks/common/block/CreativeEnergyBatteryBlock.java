package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.tile.BaseIOTile;
import io.github.ramboxeu.techworks.common.tile.CreativeEnergyBatteryTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CreativeEnergyBatteryBlock extends Block implements IWrenchable {
    public CreativeEnergyBatteryBlock() {
        super(Properties.create(Material.IRON));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CreativeEnergyBatteryTile();
    }

    @Override
    public boolean configure(World world, BlockPos pos, Direction face, Vector3d hitVec) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof BaseIOTile) {
//                MachineIO machineIO = ((BaseIOTile) te).getMachineIO();
//                if (!machineIO.isSideDisabled(Side.fromDirection(face, Utils.getFacingFromPos(world, pos)))) {
//                    // FIXME: 10/10/2020
////                    machineIO.cyclePort(face);
////                    Techworks.LOGGER.debug("{} {}", machineIO.getPort(face).getType(), machineIO.getPort(face).getMode());
////                    TechworksPacketHandler.sendMachinePortUpdatePacket(pos, face.getIndex(), machineIO.getPort(face), world.getChunkAt(pos));
//                }
            }
        }

        return true;
    }
}
