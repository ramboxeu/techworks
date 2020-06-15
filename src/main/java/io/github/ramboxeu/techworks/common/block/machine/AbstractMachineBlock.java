package io.github.ramboxeu.techworks.common.block.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import io.github.ramboxeu.techworks.common.blockstate.TechworksBlockStateProperties;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class AbstractMachineBlock extends Block implements BlockEntityProvider {
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof AbstractMachineBlockEntity<?>) {
                return ((AbstractMachineBlockEntity<?>) blockEntity).onActivated(state, world, pos, player, hand, hit);
            } else {
                Techworks.LOG.warn("Expected AbstractMachineBlockEntity on {}, but it was not found.", pos);
            }
        }

        return ActionResult.FAIL;
    }
}
