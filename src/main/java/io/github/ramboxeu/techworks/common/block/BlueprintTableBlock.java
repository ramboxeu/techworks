package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlueprintTableBlock extends Block implements IWrenchable {

    private static final VoxelShape SHAPE = VoxelShapes.or(
            VoxelShapes.create(.75d, 0, .75d, .9375d, .875d, .9375d),
            VoxelShapes.create(.0625, 0, .0625d, .25d, .625d, .25d),
            VoxelShapes.create(.75d, 0, .0625d, .9375d, .625d,.25d),
            VoxelShapes.create(.0625d, 0, .75d, .25d, .875d, .9375d),
            VoxelShapes.create(.0625, .875d, .8125, .25d, .9375d, .9375d),
            VoxelShapes.create(.0625, .875d, .8125, .25d, .9375d, .9375d),
            VoxelShapes.create(.75d, .875d, .8125, .9375d, .9375d, .9375d),
            VoxelShapes.create(.75d, .625d, .1875d, .9375d, .6875d, .25d),
            VoxelShapes.create(.0625, .625d, .1875d, .25d, .6875d, .25d),
            VoxelShapes.create(0, .75d, -.03125d, 1, .890625d, 1.03125d)
    ).simplify();

    public BlueprintTableBlock() {
        super(Properties.create(Material.WOOD)
                .setRequiresTool()
                .hardnessAndResistance(2, 3)
                .harvestLevel(1)
                .harvestTool(ToolType.AXE)
                .sound(SoundType.WOOD)
                .notSolid()
        );
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(BlockStateProperties.HORIZONTAL_FACING, direction.rotate(state.get(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public void rotate(BlockState state, BlockPos pos, @Nullable Direction face, World world) {
        if (face != null && face.getAxis().isHorizontal()) {
            world.setBlockState(pos, state.rotate(world, pos, Rotation.CLOCKWISE_90));
        }
    }

    @Override
    public void dismantle(BlockState state, BlockPos pos, World world) {}

    @Override
    public void configure(World world, BlockPos pos, Direction face) {}

    @Override
    public void openComponents(BlockPos pos, PlayerEntity player, World world) {}
}
