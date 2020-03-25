package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.inventory.InventoryBuilder;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.common.ForgeHooks;

public class BoilerContainer extends AbstractMachineContainer {
    public BoilerContainer(int id, PlayerInventory playerInventory, AbstractMachineTile tile) {
        super(Registration.BOILER_CONTAINER.get(), id, playerInventory, tile);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    protected void layoutSlots(InventoryBuilder builder) {
        builder
            .addSlot(new SlotBuilder(80, 54).predicate(PredicateUtils::isFuel))
            .addSlot(new SlotBuilder(27, 16).limit(1).predicate(PredicateUtils::isFluidHandler))
            .addSlot(new SlotBuilder(27, 54).limit(1).predicate(PredicateUtils::isFluidHandler))
            .addSlot(new SlotBuilder(133, 16).limit(1))
            .addSlot(new SlotBuilder(133, 54).limit(1));
    }
}
