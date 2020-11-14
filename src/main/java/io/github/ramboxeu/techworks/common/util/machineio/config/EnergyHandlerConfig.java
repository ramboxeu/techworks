package io.github.ramboxeu.techworks.common.util.machineio.config;

import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHandlerConfig extends HandlerConfig {
    private final EnergyHandlerData data;
    private final IEnergyStorage storage;

    public EnergyHandlerConfig(EnergyHandlerData data) {
        this(StorageMode.BOTH, data);
    }

    public EnergyHandlerConfig(StorageMode mode, EnergyHandlerData data) {
        super(mode, data);
        this.data = data;
        storage = data.getHandler();
    }

    public EnergyHandlerData getData() {
        return data;
    }

    public IEnergyStorage getStorage() {
        return storage;
    }
}
