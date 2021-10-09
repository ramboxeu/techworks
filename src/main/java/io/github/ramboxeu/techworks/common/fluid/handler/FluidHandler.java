package io.github.ramboxeu.techworks.common.fluid.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public abstract class FluidHandler implements IFluidTank, INBTSerializable<CompoundNBT> {

    protected FluidStack fluid = FluidStack.EMPTY;
    protected int capacity;
    protected int maxFill;
    protected int maxDrain;

    public FluidHandler(int capacity) {
        this(capacity, capacity);
    }

    public FluidHandler(int capacity, int transfer) {
        this(capacity, transfer, transfer);
    }

    public FluidHandler(int capacity, int maxFill, int maxDrain) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
    }

    @Nonnull
    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    @Override
    public int getFluidAmount() {
        return fluid.getAmount();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action, boolean ignoreMaxFill) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }

        if (!fluid.isFluidEqual(resource) && !fluid.isEmpty()) {
            return 0;
        }

        int fill = Math.min(capacity - fluid.getAmount(), resource.getAmount());
        int maxedFill = ignoreMaxFill ? fill : Math.min(fill, maxFill);

        if (action.execute()) {
            if (fluid.isEmpty()) {
                fluid = new FluidStack(resource, maxedFill);
            } else {
                fluid.grow(maxedFill);
            }
            onContentsChanged();
        }

        return maxedFill;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        return fill(resource, action, false);
    }

    public FluidStack drain(int amount, IFluidHandler.FluidAction action, boolean ignoreMaxDrain) {
        if (fluid.isEmpty() || amount <= 0) {
            return FluidStack.EMPTY;
        }

        int drain = Math.min(fluid.getAmount(), amount);
        int maxedDrain = ignoreMaxDrain ? drain : Math.min(drain, maxDrain);

        FluidStack returned = new FluidStack(fluid, maxedDrain);

        if (action.execute()) {
            fluid.shrink(maxedDrain);
            onContentsChanged();
        }

        return returned;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty() || !fluid.isFluidEqual(resource)) {
            return FluidStack.EMPTY;
        }

        return drain(resource.getAmount(), action, false);
    }

    @Nonnull
    @Override
    public FluidStack drain(int amount, IFluidHandler.FluidAction action) {
        return drain(amount, action, false);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.put("Fluid", fluid.writeToNBT(new CompoundNBT()));

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid"));
    }

    protected void onContentsChanged() {
    }

    public int getMaxFill() {
        return maxFill;
    }

    public int getMaxDrain() {
        return maxDrain;
    }
}
