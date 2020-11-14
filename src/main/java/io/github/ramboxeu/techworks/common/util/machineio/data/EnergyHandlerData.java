package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHandlerData extends HandlerData {
    private final IEnergyStorage handler;

    public EnergyHandlerData(InputType type, int color, int identity, IEnergyStorage handler) {
        super(type, color, identity, handler);
        this.handler = handler;
    }

    public IEnergyStorage getHandler() {
        return handler;
    }
}
