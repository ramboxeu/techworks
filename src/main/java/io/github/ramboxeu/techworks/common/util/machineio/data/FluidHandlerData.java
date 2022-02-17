package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraftforge.fluids.IFluidTank;

public abstract class FluidHandlerData extends HandlerData {
    public FluidHandlerData(InputType type, int color, int identity, IFluidTank handler, StorageMode supportedMode) {
        super(type, color, identity, handler, supportedMode);
    }

    public abstract IFluidTank getFluidTank();
}
