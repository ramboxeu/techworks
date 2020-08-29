package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.tile.CreativeEnergyBatteryTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CreativeEnergyBatteryBlock extends Block {
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
}
