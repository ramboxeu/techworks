package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.tile.ElectricFurnaceTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ElectricFurnaceBlock extends AbstractMachineBlock {
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElectricFurnaceTile();
    }
}
