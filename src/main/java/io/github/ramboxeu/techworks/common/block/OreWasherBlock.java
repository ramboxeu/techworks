package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.tile.machine.OreWasherTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class OreWasherBlock extends BaseMachineBlock {

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new OreWasherTile();
    }
}
