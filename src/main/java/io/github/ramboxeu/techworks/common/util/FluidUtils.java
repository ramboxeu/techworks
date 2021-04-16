package io.github.ramboxeu.techworks.common.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidUtils {

    public static FluidStack firstNotEmpty(IFluidHandler handler, int maxAmount) {
        for (int i = 0; i < handler.getTanks(); i++) {
            FluidStack stack = handler.getFluidInTank(i);

            if (!stack.isEmpty())
                return handler.drain(maxAmount, IFluidHandler.FluidAction.EXECUTE);
        }

        return FluidStack.EMPTY;
    }

    public static boolean isGas(FluidStack stack) {
        return stack.getFluid().getAttributes().isGaseous(stack);
    }

    public static boolean isLiquid(FluidStack stack) {
        return !stack.getFluid().getAttributes().isGaseous(stack);
    }
}
