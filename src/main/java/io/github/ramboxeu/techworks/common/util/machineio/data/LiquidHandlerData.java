package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.capability.impl.ILiquidTank;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;

public class LiquidHandlerData extends HandlerData {
    private final ILiquidTank handler;

    public LiquidHandlerData(InputType type, int color, int identity, ILiquidTank handler) {
        super(type, color, identity, handler);
        this.handler = handler;
    }

    public ILiquidTank getHandler() {
        return handler;
    }
}
