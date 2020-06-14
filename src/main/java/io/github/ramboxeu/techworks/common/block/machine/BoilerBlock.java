package io.github.ramboxeu.techworks.common.block.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BoilerBlock extends AbstractMachineBlock {
    public BoilerBlock() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new BoilerBlockEntity();
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        world.removeBlockEntity(pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ((BoilerBlockEntity) world.getBlockEntity(pos)).onRightClick(player.isSneaking());

        return ActionResult.SUCCESS;
    }
}