package io.github.ramboxeu.techworks.common.block.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class BoilerBlock extends AbstractMachineBlock {
    public BoilerBlock() {
        super();
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new BoilerBlockEntity();
    }
}
