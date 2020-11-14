package io.github.ramboxeu.techworks.common.util.machineio.config;

import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;

public abstract class HandlerConfig {
    protected StorageMode mode;
    protected io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData baseData;

    public HandlerConfig(StorageMode mode, HandlerData baseData) {
        this.mode = mode;
        this.baseData = baseData;
    }

    public StorageMode getMode() {
        return mode;
    }

    public void setMode(StorageMode mode) {
        this.mode = mode;
    }

    public HandlerData getBaseData() {
        return baseData;
    }
}
