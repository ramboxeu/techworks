package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.inventory.InventoryBuilder;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;

public class ElectricGrinderContainer extends AbstractMachineContainer {
    private int workTime;
    private int workCounter;

    public ElectricGrinderContainer(int id, PlayerInventory playerInventory, AbstractMachineTile machineTile) {
        super(Registration.ELECTRIC_GRINDER_CONTAINER.get(), id, playerInventory, machineTile);

        this.trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return machineTile.getOperationTime();
            }

            @Override
            public void set(int time) {
                workTime = time;
            }
        });

        this.trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return machineTile.getTimeCounter();
            }

            @Override
            public void set(int counter) {
                workCounter = counter;
            }
        });
    }

    @Override
    protected void layoutSlots(InventoryBuilder builder) {
        builder.addSlot(new SlotBuilder(32, 53).predicate(PredicateUtils::isEnergyStorage))
                .addSlot(new SlotBuilder(62, 35))
                .addSlot(new SlotBuilder(122, 35).output(true));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public int getWorkTime() {
        return this.workTime;
    }

    public int getWorkCounter() {
        return this.workCounter;
    }
}
