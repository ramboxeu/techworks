package io.github.ramboxeu.techworks.common.capability.impl;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface ILiquidHolder extends IFluidHandler {
    @Override
    default boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return stack.getFluid().getAttributes().isGaseous(stack) && isLiquidValid(tank, stack);
    }

    boolean isLiquidValid(int tank, @NotNull FluidStack stack);
}
