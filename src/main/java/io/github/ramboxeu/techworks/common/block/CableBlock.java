package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.client.model.cable.CableModel;
import io.github.ramboxeu.techworks.common.registry.TileRegistryObject;
import io.github.ramboxeu.techworks.common.tile.BaseCableTile;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static io.github.ramboxeu.techworks.client.model.cable.CableModel.*;

public class CableBlock extends Block implements IWrenchable {
    private static final VoxelShape CENTER = CableModel.CENTER.getVoxelShape();

    private final TileRegistryObject<?> tile;

    public CableBlock(TileRegistryObject<?> tile) {
        super(Properties.create(Material.IRON));

        this.tile = tile;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tile.get().create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof BaseCableTile) {
            CableConnections connections = ((BaseCableTile) te).getConnections();

            VoxelShape north = NORTH.chooseVoxelShape(connections.getConnection(Direction.NORTH));
            VoxelShape south = SOUTH.chooseVoxelShape(connections.getConnection(Direction.SOUTH));
            VoxelShape east = EAST.chooseVoxelShape(connections.getConnection(Direction.EAST));
            VoxelShape west = WEST.chooseVoxelShape(connections.getConnection(Direction.WEST));
            VoxelShape up = UP.chooseVoxelShape(connections.getConnection(Direction.UP));
            VoxelShape down = DOWN.chooseVoxelShape(connections.getConnection(Direction.DOWN));

            return VoxelShapes.or(CENTER, north, south, east, west, up, down);
        }

        return CENTER;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbourPos, boolean isMoving) {
        Direction direction = MathUtils.getDirectionFromPos(pos, neighbourPos);

        if (direction != null)
            updateConnections(direction, world, pos);

        super.neighborChanged(state, world, pos, block, neighbourPos, isMoving);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return super.getRenderShape(state, worldIn, pos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighbour, IWorld world, BlockPos pos, BlockPos neighbourPos) {
        updateConnections(facing, world, pos);
        return super.updatePostPlacement(state, facing, neighbour, world, pos, neighbourPos);
    }

    private void updateConnections(Direction facing, IWorld world, BlockPos pos) {
        if (!world.isRemote()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof BaseCableTile) {
                ((BaseCableTile) tile).updateConnections(facing);
            }
        }
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        return true;
    }
}
