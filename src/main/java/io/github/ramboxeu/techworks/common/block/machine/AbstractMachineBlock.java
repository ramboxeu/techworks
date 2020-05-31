package io.github.ramboxeu.techworks.common.block.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import io.github.ramboxeu.techworks.common.blockstate.TechworksBlockStateProperties;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public abstract class AbstractMachineBlock extends BlockWithEntity {
    public AbstractMachineBlock() {
        super(FabricBlockSettings.of(Material.METAL).build());

        this.setDefaultState(this.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(TechworksBlockStateProperties.RUNNING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, TechworksBlockStateProperties.RUNNING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(TechworksBlockStateProperties.RUNNING, false).with(TechworksBlockStateProperties.FACING, ctx.getPlayerFacing().getOpposite());
    }
}