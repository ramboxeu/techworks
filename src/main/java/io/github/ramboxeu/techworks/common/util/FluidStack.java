package io.github.ramboxeu.techworks.common.util;

import net.minecraft.fluid.Fluid;

// This is solely for EventEmitter and other garbage testing, proper fluids are yet to be implemented
public class FluidStack {
    private final Fluid fluid;
    private final int amount;

    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getAmount() {
        return amount;
    }
}
