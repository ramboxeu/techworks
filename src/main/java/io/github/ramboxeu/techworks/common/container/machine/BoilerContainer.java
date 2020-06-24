package io.github.ramboxeu.techworks.common.container.machine;

import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class BoilerContainer extends AbstractMachineContainer<BoilerBlockEntity> {

    public BoilerContainer(int syncId, PlayerInventory inventory, BoilerBlockEntity blockEntity, int dataSize) {
        super(TechworksContainers.BOILER, syncId, inventory, blockEntity, dataSize);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
