package io.github.ramboxeu.techworks.common.block.cable;

import io.github.ramboxeu.techworks.common.tile.cable.BasicGasPipeTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BasicGasPipeBlock extends AbstractCableBlock {
    public BasicGasPipeBlock() {}

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BasicGasPipeTile();
    }
}
