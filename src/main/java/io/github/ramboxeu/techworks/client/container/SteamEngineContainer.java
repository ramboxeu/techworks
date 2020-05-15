package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class SteamEngineContainer extends AbstractMachineContainer {
    public SteamEngineContainer(int id, PlayerInventory playerInventory, AbstractMachineTile machineTile) {
        super(Registration.STEAM_ENGINE_CONTAINER.get(), id, playerInventory, machineTile);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
