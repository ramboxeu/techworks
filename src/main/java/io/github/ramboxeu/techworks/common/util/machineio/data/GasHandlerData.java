package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.capability.impl.IGasTank;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import net.minecraftforge.fluids.IFluidTank;

public class GasHandlerData extends FluidHandlerData {
    private final IGasTank handler;

    public GasHandlerData(InputType type, int color, int identity, IGasTank handler) {
        super(type, color, identity, handler);
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
