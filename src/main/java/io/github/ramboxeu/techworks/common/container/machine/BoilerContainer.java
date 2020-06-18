package io.github.ramboxeu.techworks.common.container.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.ArrayPropertyDelegate;
import net.minecraft.container.Property;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class BoilerContainer extends AbstractMachineContainer<BoilerBlockEntity> {

    public BoilerContainer(int syncId, PlayerInventory inventory, BoilerBlockEntity blockEntity, int dataSize) {
        super(syncId, inventory, blockEntity, dataSize);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public int getDummyInt() {
        return getSyncedValueOrDefault(0, -1);
    }
}
