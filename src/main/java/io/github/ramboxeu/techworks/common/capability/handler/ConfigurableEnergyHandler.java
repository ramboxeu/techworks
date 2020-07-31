package io.github.ramboxeu.techworks.common.capability.handler;

import io.github.ramboxeu.techworks.common.tile.machine.MachinePort;
import net.minecraftforge.energy.IEnergyStorage;

public class ConfigurableEnergyHandler implements IEnergyStorage {
    private MachinePort.Mode mode;
    private IEnergyStorage parent;

    public ConfigurableEnergyHandler(MachinePort.Mode mode, IEnergyStorage parent) {
        this.mode = mode;
        this.parent = parent;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return mode.canInput() ? parent.receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return mode.canOutput() ? parent.extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored() {
        return parent.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return parent.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return mode.canOutput();
    }

    @Override
    public boolean canReceive() {
        return mode.canInput();
    }

    public MachinePort.Mode getMode() {
        return mode;
    }
}
