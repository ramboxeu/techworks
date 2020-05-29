package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.common.container.machine.AbstractMachineContainer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public abstract class AbstractMachineBlockEntity<TEntity extends AbstractMachineBlockEntity<?, ?>, TContainer extends AbstractMachineContainer<TEntity>> extends BlockEntity {
    public AbstractMachineBlockEntity(BlockEntityType<? extends TEntity> type) {
        super(type);
    }
}
