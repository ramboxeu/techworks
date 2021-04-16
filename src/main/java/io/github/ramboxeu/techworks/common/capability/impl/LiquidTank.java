package io.github.ramboxeu.techworks.common.capability.impl;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class LiquidTank implements ILiquidTank {
    protected FluidStack storedGas = FluidStack.EMPTY;
    protected int capacity;

    public LiquidTank(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean isLiquidValid(FluidStack stack) {
        return true;
    }

    @NotNull
    @Override
    public FluidStack getFluid() {
        return storedGas;
    }

    @Override
    public int getFluidAmount() {
        return storedGas.getAmount();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }

        if (!storedGas.isFluidEqual(resource) && !storedGas.isEmpty()) {
            return 0;
        }

        int fill = Math.min(capacity - storedGas.getAmount(), resource.getAmount());

        if (action.execute()) {
            if (storedGas.isEmpty()) {
                storedGas = new FluidStack(resource, fill);
            } else {
                storedGas.grow(fill);
            }
        }

        return fill;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty() || !storedGas.isFluidEqual(resource)) {
            return FluidStack.EMPTY;
        }

        return drain(resource.getAmount(), action);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (storedGas.isEmpty() || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        int drain = Math.min(storedGas.getAmount(), maxDrain);

        FluidStack returned = new FluidStack(storedGas, drain);

        if (action.execute()) {
            storedGas.shrink(drain);
        }

        return returned;
    }
}
