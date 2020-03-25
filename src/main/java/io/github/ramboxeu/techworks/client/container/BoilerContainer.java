package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BoilerContainer extends AbstractMachineContainer {
    public BoilerContainer(int id, PlayerInventory playerInventory, AbstractMachineTile tile) {
        super(Registration.BOILER_CONTAINER.get(), id, playerInventory, tile);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    protected void layoutSlots(IItemHandler handler) {
        this.addSlot(new SlotItemHandler(handler, 0, 0, 0));

        super.layoutSlots(handler);
    }
}
