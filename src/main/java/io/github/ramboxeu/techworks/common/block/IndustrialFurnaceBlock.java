package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.tile.machine.IndustrialFurnaceTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IndustrialFurnaceBlock extends BaseMachineBlock {
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IndustrialFurnaceTile();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbourPos, boolean isMoving) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IndustrialFurnaceTile) {
                ((IndustrialFurnaceTile) tile).onNeighbourChange(world, neighbourPos);
            }
        }

        super.neighborChanged(state, world, pos, block, neighbourPos, isMoving);
    }
}
