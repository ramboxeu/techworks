package io.github.ramboxeu.techworks.common.util.capability;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class FluidWrapper implements IFluidHandler {
    private IFluidHandler handler;
    private boolean canInsert;
    private boolean canExtract;

    public FluidWrapper(IFluidHandler handler, boolean canInsert, boolean canExtract) {
        this.handler = handler;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }


    @Override
    public int getTanks() {
        return handler.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return handler.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return handler.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return handler.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return canInsert ? handler.fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return canExtract ? handler.drain(resource, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return canExtract ? handler.drain(maxDrain, action) : FluidStack.EMPTY;
    }
}
