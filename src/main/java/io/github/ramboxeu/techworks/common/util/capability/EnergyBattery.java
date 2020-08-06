package io.github.ramboxeu.techworks.common.util.capability;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyBattery implements IEnergyStorage {
    protected int energy = 0;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyBattery(int capacity) {
        this(capacity, capacity, capacity);
    }

    public EnergyBattery(int capacity, int maxReceive, int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }

        int received = Math.min(energy, Math.max(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += received;
            onContentsChanged();
        }

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }

        int received = Math.min(energy, Math.max(this.maxExtract, maxExtract));
        if (!simulate) {
            energy -= received;
            onContentsChanged();
        }

        return received;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    protected void onContentsChanged() {}

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setTransfer(int maxReceive, int maxExtract) {
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
    }
}
