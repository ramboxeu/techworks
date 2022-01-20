package io.github.ramboxeu.techworks.common.energy;

import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageWrapper implements IEnergyStorage {
    private final IEnergyStorage delegate;
    private StorageMode mode = StorageMode.BOTH;

    public EnergyStorageWrapper(IEnergyStorage delegate) {
        this.delegate = delegate;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return mode.canInput() ? delegate.receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return mode.canOutput() ? delegate.receiveEnergy(maxExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored() {
        return delegate.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return delegate.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return mode.canOutput();
    }

    @Override
    public boolean canReceive() {
        return mode.canInput();
    }

    public void setMode(StorageMode mode) {
        this.mode = mode;
    }

    public StorageMode getMode() {
        return mode;
    }
}
