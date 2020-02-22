package io.github.ramboxeu.techworks.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class BoilerBlock extends Block {
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BoilerBlock() {
        super(Properties.create(Material.IRON));

        this.setRegistryName("boiler");
        this.setDefaultState(this.stateContainer.getBaseState().with(RUNNING, Boolean.valueOf(false)).with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(RUNNING, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}
