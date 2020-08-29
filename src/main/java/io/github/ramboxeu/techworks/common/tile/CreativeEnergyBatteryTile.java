package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.machine.MachineIO;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort;
import net.minecraftforge.energy.IEnergyStorage;

public class CreativeEnergyBatteryTile extends BaseIOTile implements IEnergyStorage {

    public CreativeEnergyBatteryTile() {
        super(TechworksTiles.CREATIVE_ENERGY_BATTERY.getTileType());

        machineIO = MachineIO.create(MachineIO.PortConfig.create(MachinePort.Type.ENERGY, this));
    }

    // IEnergyStorage
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
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
        return false;
    }
}
