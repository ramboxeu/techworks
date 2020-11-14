package io.github.ramboxeu.techworks.common.capability.impl;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface ILiquidTank extends IFluidTank {
    boolean isLiquidValid(FluidStack stack);

    @Override
    default boolean isFluidValid(FluidStack stack) {
        return isLiquidValid(stack) && !stack.getFluid().getAttributes().isGaseous(stack);
    }
}
