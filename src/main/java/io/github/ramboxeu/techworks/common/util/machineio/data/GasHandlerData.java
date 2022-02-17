package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.fluid.handler.IGasTank;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraftforge.fluids.IFluidTank;

public class GasHandlerData extends FluidHandlerData {
    private final IGasTank handler;

    public GasHandlerData(InputType type, int color, int identity, IGasTank handler, StorageMode supportedMode) {
        super(type, color, identity, handler, supportedMode);
        this.handler = handler;
    }

    public IGasTank getHandler() {
        return handler;
    }

    @Override
    public IFluidTank getFluidTank() {
        return handler;
    }
}
