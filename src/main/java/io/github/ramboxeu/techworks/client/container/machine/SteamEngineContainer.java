package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.display.EnergyDisplayWidget;
import io.github.ramboxeu.techworks.common.registration.TechworksContainers;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class SteamEngineContainer extends BaseMachineContainer<SteamEngineTile> {
    public SteamEngineContainer(int id, PlayerInventory playerInventory, SteamEngineTile tile) {
        super(TechworksContainers.STEAM_ENGINE.get(), id, playerInventory, tile);

        addWidget(new EnergyDisplayWidget(this, 7, 14, tile.getBatteryData()));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
