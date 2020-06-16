package io.github.ramboxeu.techworks.common.container.machine;

import io.github.ramboxeu.techworks.common.api.component.ComponentInventory;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class MachineComponentsContainer extends Container {
    private final AbstractMachineBlockEntity<?> machineBlockEntity;
    protected PlayerInventory playerInventory;
    private static final int BASE_X = 0;
    private static final int BASE_Y = 0;
    private static final int SLOT_WIDTH = 18;
    private static final int SLOT_HEIGHT = 18;
    private static final int SPACING = 4;

    public MachineComponentsContainer(int syncId, PlayerInventory playerInventory, AbstractMachineBlockEntity<?> machineBlockEntity) {
        super(null, syncId);
        this.machineBlockEntity = machineBlockEntity;
        this.playerInventory = playerInventory;

        ComponentInventory<?> componentInventory = machineBlockEntity.getComponentList();

        for (int i = 0; i < componentInventory.getInvSize(); i++) {
            this.addSlot(new Slot(componentInventory, i, (BASE_X + SLOT_WIDTH + SPACING) * i, BASE_Y));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }
}
