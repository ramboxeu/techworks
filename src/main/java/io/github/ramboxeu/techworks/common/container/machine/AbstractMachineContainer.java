package io.github.ramboxeu.techworks.common.container.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;

public abstract class AbstractMachineContainer<T extends AbstractMachineBlockEntity> extends Container {
    protected T blockEntity;
    protected PlayerInventory playerInventory;

    public AbstractMachineContainer(int syncId, PlayerInventory playerInventory, T blockEntity) {
        super(null, syncId);
        this.blockEntity = blockEntity;
        this.playerInventory = playerInventory;
    }
}
