package io.github.ramboxeu.techworks.common.capability.impl;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class DevEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundNBT> {
    private final DevBlockTile tile;

    public DevEnergyStorage(DevBlockTile tile) {
        this.tile = tile;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        tile.createLog("E R " + (simulate ? "sim " : "exe ") + maxReceive);
        return maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
//        int extracted = Math.min(maxExtract, this.maxExtract);
        tile.createLog("E E " + (simulate ? "sim " : "exe ") + maxExtract);
        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
