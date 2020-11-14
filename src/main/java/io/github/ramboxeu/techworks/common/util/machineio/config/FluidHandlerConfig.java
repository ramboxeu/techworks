package io.github.ramboxeu.techworks.common.util.machineio.config;

import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.data.GasHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
import net.minecraftforge.fluids.IFluidTank;

public class FluidHandlerConfig extends HandlerConfig {
    private final IFluidTank tank;

    public FluidHandlerConfig(LiquidHandlerData data) {
        this(StorageMode.BOTH, data);
    }

    public FluidHandlerConfig(GasHandlerData data) {
        this(StorageMode.BOTH, data);
    }

    public FluidHandlerConfig(StorageMode mode, LiquidHandlerData data) {
        super(mode, data);
        tank = data.getHandler();
    }

    public FluidHandlerConfig(StorageMode mode, GasHandlerData data) {
        super(mode, data);
        tank = data.getHandler();
    }

    public IFluidTank getTank() {
        return tank;
    }
}
