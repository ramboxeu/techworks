package io.github.ramboxeu.techworks.common.fluid.handler;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IGasTank extends IFluidTank {

    boolean isGasValid(FluidStack stack);

    @Override
    default boolean isFluidValid(FluidStack stack) {
        return isGasValid(stack) && stack.getFluid().getAttributes().isGaseous(stack);
    }
}
