package io.github.ramboxeu.techworks.common.util.machineio.config;

import io.github.ramboxeu.techworks.common.fluid.handler.IGasTank;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.data.GasHandlerData;

public class GasHandlerConfig extends HandlerConfig {
    private final GasHandlerData data;
    private final IGasTank tank;

    public GasHandlerConfig(StorageMode mode, GasHandlerData data) {
        super(mode, data);
        this.data = data;
        tank = data.getHandler();
    }

    public GasHandlerData getData() {
        return data;
    }

    public IGasTank getTank() {
        return tank;
    }
}
