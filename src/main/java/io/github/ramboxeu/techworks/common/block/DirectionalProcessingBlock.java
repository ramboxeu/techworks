package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

public abstract class DirectionalProcessingBlock extends Block {

    public DirectionalProcessingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, TechworksBlockStateProperties.RUNNING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
                .with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite())
                .with(TechworksBlockStateProperties.RUNNING, false);
    }

}
