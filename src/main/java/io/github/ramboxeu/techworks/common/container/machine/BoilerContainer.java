package io.github.ramboxeu.techworks.common.container.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class BoilerContainer extends AbstractMachineContainer<BoilerBlockEntity> {
    public BoilerContainer(int syncId, PlayerInventory inventory, BoilerBlockEntity blockEntity) {
        super(syncId, inventory, blockEntity);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
