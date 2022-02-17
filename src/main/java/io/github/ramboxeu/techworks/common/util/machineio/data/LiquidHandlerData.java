package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.fluid.handler.ILiquidTank;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraftforge.fluids.IFluidTank;

public class LiquidHandlerData extends FluidHandlerData {
    private final ILiquidTank handler;

    public LiquidHandlerData(InputType type, int color, int identity, ILiquidTank handler, StorageMode supportedMode) {
        super(type, color, identity, handler, supportedMode);
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
