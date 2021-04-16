package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import net.minecraftforge.fluids.IFluidTank;

public abstract class FluidHandlerData extends HandlerData {
    public FluidHandlerData(InputType type, int color, int identity, IFluidTank handler) {
        super(type, color, identity, handler);
    }

    public abstract IFluidTank getFluidTank();
}
