package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.client.container.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class SteamEngineTile extends AbstractMachineTile {
    public SteamEngineTile() {
        super(Registration.STEAM_ENGINE_TILE.get(), 200);
    }

    @Override
    void run() {
        this.gasHandler.ifPresent(gas -> {
            this.energyStorage.ifPresent(energy -> {
                if (gas.extractGas(Registration.STEAM_GAS.get(), 100, false) == 100) {
                    energy.receiveEnergy(100, false);
                }
            });
        });
    }

    @Override
    public boolean hasEnergyStorage() {
        return true;
    }

    @Override
    public boolean hasGasHandler() {
        return true;
    }

    @Override
    protected IEnergyStorage createEnergyStorage() {
        return new EnergyStorage(1000, 100);
    }

    @Override
    protected IGasHandler createGasHandler() {
        return new GasHandler(Registration.STEAM_GAS.get(), 200, 10000) {
            @Override
            public void onContentsChanged() {
                markDirty();
            }
        };
    }

    @Override
    boolean canWork() {
        return (this.gasHandler.orElseGet(GasHandler::new).extractGas(Registration.STEAM_GAS.get(), 100, true) == 100) &&
                (this.energyStorage.orElseGet(() -> new EnergyStorage(0,0,0, 0)).receiveEnergy(100, true) == 100);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.steam_engine");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new SteamEngineContainer(id, inventory, this);
    }
}
