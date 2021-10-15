package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MetalPressBlock extends BaseMachineBlock {
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MetalPressTile();
    }
}
