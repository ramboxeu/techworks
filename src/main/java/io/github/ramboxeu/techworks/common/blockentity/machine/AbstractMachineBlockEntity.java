package io.github.ramboxeu.techworks.common.blockentity.machine;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.util.Tickable;

public abstract class AbstractMachineBlockEntity<TEntity extends AbstractMachineBlockEntity<TEntity>> extends BlockEntity implements NameableContainerFactory, Tickable {
    protected ComponentInventory<TEntity> componentList;

    public AbstractMachineBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        componentList.tick();
    }

    public ComponentInventory<TEntity> getComponentList() {
        return componentList;
    }
}
