package io.github.ramboxeu.techworks.common.capability.impl;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class ConfigurableFluidHandler implements IFluidHandler {
    private MachinePort.Mode mode;
    private IFluidHandler parent;

    public ConfigurableFluidHandler(MachinePort.Mode mode, IFluidHandler parent) {
        this.mode = mode;
        this.parent = parent;
    }

    @Override
    public int getTanks() {
        return parent.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (mode.canOutput()) {
            return parent.getFluidInTank(tank);
        } else {
            Techworks.LOGGER.warn("Attempted to get stack when input mode");
            return FluidStack.EMPTY;
        }
    }

    @Override
    public int getTankCapacity(int tank) {
        return parent.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return parent.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return mode.canInput() ? parent.fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource.getAmount(), action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return mode.canOutput() ? parent.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    public MachinePort.Mode getMode() {
        return mode;
    }
}
