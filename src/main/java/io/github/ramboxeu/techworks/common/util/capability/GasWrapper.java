package io.github.ramboxeu.techworks.common.util.capability;

import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.common.gas.Gas;
import net.minecraft.nbt.CompoundNBT;

public class GasWrapper implements IGasHandler {
    private IGasHandler handler;
    private boolean canInsert;
    private boolean canExtract;

    public GasWrapper(IGasHandler handler, boolean canExtract, boolean canInsert) {
        this.handler = handler;
        this.canExtract = canExtract;
        this.canInsert = canInsert;
    }

    @Override
    public Gas getGas() {
        return handler.getGas();
    }

    @Override
    public int insertGas(Gas gas, int amount, boolean simulate) {
        return canInsert ? handler.insertGas(gas, amount, simulate) : 0;
    }

    @Override
    public int extractGas(Gas gas, int amount, boolean simulate) {
        return canExtract ? handler.extractGas(gas, amount, simulate) : 0;
    }

    @Override
    public int getMaxStorage() {
        return handler.getMaxStorage();
    }

    @Override
    public int getAmountStored() {
        return handler.getAmountStored();
    }

    @Override
    public boolean canExtract() {
        return handler.canExtract();
    }

    @Override
    public boolean canInsert() {
        return handler.canExtract();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        handler.deserializeNBT(nbt);
    }
}
