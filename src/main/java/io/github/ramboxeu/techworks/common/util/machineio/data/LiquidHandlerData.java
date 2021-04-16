package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.capability.impl.ILiquidTank;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import net.minecraftforge.fluids.IFluidTank;

public class LiquidHandlerData extends FluidHandlerData {
    private final ILiquidTank handler;

    public LiquidHandlerData(InputType type, int color, int identity, ILiquidTank handler) {
        super(type, color, identity, handler);
        this.handler = handler;
    }

    public ILiquidTank getHandler() {
        return handler;
    }

    @Override
    public IFluidTank getFluidTank() {
        return handler;
    }
}
