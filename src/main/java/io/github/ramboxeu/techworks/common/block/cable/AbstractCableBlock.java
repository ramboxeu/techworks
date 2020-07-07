package io.github.ramboxeu.techworks.common.block.cable;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.tile.cable.AbstractCableTile;
import io.github.ramboxeu.techworks.common.util.CableConnections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractCableBlock extends Block {
    private static final VoxelShape CENTER = VoxelShapes.create(0.35, 0.35, 0.35, 0.65, 0.65, 0.65);
    private static final VoxelShape UP = VoxelShapes.create(0.35, 0.65, 0.35, 0.65, 1, 0.65);
    private static final VoxelShape DOWN = VoxelShapes.create(0.35, 0.35, 0.35, 0.65, 0, 0.65);
    private static final VoxelShape WEST = VoxelShapes.create(0.35, 0.35, 0.35, 0, 0.65, 0.65);
    private static final VoxelShape EAST = VoxelShapes.create(0.65, 0.35, 0.35, 1, 0.65, 0.65);
    private static final VoxelShape NORTH = VoxelShapes.create(0.35, 0.35, 0.35, 0.65, 0.65, 0);
    private static final VoxelShape SOUTH = VoxelShapes.create(0.35, 0.35, 0.65, 0.65, 0.65, 1);

    public AbstractCableBlock() {
        super(Properties.create(Material.IRON));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof AbstractCableTile) {
            ArrayList<VoxelShape> shapes = new ArrayList<>();
            CableConnections connections = ((AbstractCableTile) te).getConnections();
            //Techworks.LOGGER.debug(Arrays.toString(connections.getAsArray()));

            if (connections.isUp()) shapes.add(UP);
            if (connections.isDown()) shapes.add(DOWN);
            if (connections.isNorth()) shapes.add(NORTH);
            if (connections.isSouth()) shapes.add(SOUTH);
            if (connections.isWest()) shapes.add(WEST);
            if (connections.isEast()) shapes.add(EAST);

            return VoxelShapes.or(CENTER, shapes.toArray(new VoxelShape[0]));
        }

        return CENTER;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof AbstractCableTile) {
            ((AbstractCableTile) te).updateConnections(true);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof AbstractCableTile) {
            ((AbstractCableTile) te).updateConnections(false);
        }
    }
}
