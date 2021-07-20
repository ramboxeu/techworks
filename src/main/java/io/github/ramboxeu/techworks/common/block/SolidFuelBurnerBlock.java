package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.tile.SolidFuelBurnerTile;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SolidFuelBurnerBlock extends DirectionalProcessingBlock {
    public SolidFuelBurnerBlock() {
        super(Properties.create(Material.IRON));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SolidFuelBurnerTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return Utils.openTileContainer(world.getTileEntity(pos), player, world);
    }
}
