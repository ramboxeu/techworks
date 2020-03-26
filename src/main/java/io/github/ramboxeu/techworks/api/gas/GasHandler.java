package io.github.ramboxeu.techworks.api.gas;

import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/*
    Heavily inspired by Forge's EnergyStorage

    Use if you want to handle gases
 */
public class GasHandler implements IGasHandler, INBTSerializable<CompoundNBT> {
    public Gas gas;
    private int maxExtract;
    private int maxInsert;
    private int capacity;
    public int amountStored;

    public GasHandler(Gas gas, int maxInsert, int maxExtract, int capacity) {
        this.gas = gas;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
        this.capacity = capacity;
    }

    public GasHandler(Gas gas, int maxTransfer, int capacity) {
        this(gas, maxTransfer, maxTransfer, capacity);
    }

    public GasHandler() {
        this(Registration.EMPTY_GAS.get(), 0, 0,0);
    }

    @Override
    public Gas getGas() {
        return gas;
    }

    @Override
    public int extractGas(Gas gas, int amount, boolean simulate) {
        if (!gas.equals(this.gas) || !canExtract()) {
            return 0;
        }

        int gasExtracted = Math.min(this.amountStored, Math.min(amount, this.maxExtract));

        if (!simulate) {
            this.amountStored -= gasExtracted;
        }

        this.onContentsChanged();
        return gasExtracted;
    }

    @Override
    public int insertGas(Gas gas, int amount, boolean simulate) {
        if (!gas.equals(this.gas) || !canInsert()) {
            return 0;
        }

        int gasInserted = Math.min(this.capacity - this.amountStored, Math.min(amount, this.maxExtract));

        if (!simulate) {
            this.amountStored += gasInserted;
        }

        this.onContentsChanged();
        return gasInserted;
    }

    @Override
    public int getMaxStorage() {
        return capacity;
    }

    @Override
    public int getStorage() {
        return amountStored;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canInsert() {
        return maxInsert > 0;
    }

    public void onContentsChanged(){}

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CapabilityGas.GAS.writeNBT(this, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CapabilityGas.GAS.readNBT(this, null, nbt);
    }
}
