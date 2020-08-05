package io.github.ramboxeu.techworks.common.util.capability;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

// My own system gets in my way, yay I'm dumb
@SuppressWarnings("ConstantConditions")
public class EnergyWrapper implements IEnergyStorage {
    private LazyOptional<IEnergyStorage> storage;

    public EnergyWrapper(LazyOptional<IEnergyStorage> storage) {
        this.storage = storage;
    }


    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storage.isPresent() ? storage.orElse(null).receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.isPresent() ? storage.orElse(null).extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored() {
        return storage.isPresent() ? storage.orElse(null).getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.isPresent() ? storage.orElse(null).getMaxEnergyStored() : 0;
    }

    @Override
    public boolean canExtract() {
        return storage.isPresent() && storage.orElse(null).canExtract();
    }

    @Override
    public boolean canReceive() {
        return storage.isPresent() && storage.orElse(null).canReceive();
    }

    public void setHandler(LazyOptional<IEnergyStorage> handler) {
        storage = handler;
    }
}
