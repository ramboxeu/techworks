package io.github.ramboxeu.techworks.common.util.capability;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyWrapper implements IEnergyStorage {
    private IEnergyStorage storage;
    private boolean canInsert;
    private boolean canExtract;

    public EnergyWrapper(IEnergyStorage storage, boolean canInsert, boolean canExtract) {
        this.storage = storage;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }


    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return canInsert ? storage.receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return canExtract ? storage.extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return storage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return storage.canReceive();
    }
}
