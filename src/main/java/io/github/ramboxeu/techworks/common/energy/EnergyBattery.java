package io.github.ramboxeu.techworks.common.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyBattery implements IEnergyStorage, INBTSerializable<CompoundNBT> {
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyBattery(int capacity) {
        this(capacity, capacity, capacity);
    }

    public EnergyBattery(int capacity, int transfer) {
        this(capacity, transfer, transfer);
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

        int received = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
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

        int extracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energy -= extracted;
            onContentsChanged();
        }

        return extracted;
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

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putInt("Energy", energy);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        energy = nbt.getInt("Energy");
    }
}
