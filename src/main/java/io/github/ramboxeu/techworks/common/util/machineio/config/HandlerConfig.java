package io.github.ramboxeu.techworks.common.util.machineio.config;

import io.github.ramboxeu.techworks.common.util.machineio.AutoMode;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;

public abstract class HandlerConfig {
    protected StorageMode mode;
    protected AutoMode autoMode;
    protected HandlerData baseData;

    public HandlerConfig(StorageMode mode, AutoMode autoMode, HandlerData baseData) {
        this.mode = mode;
        this.autoMode = autoMode;
        this.baseData = baseData;
    }

    public StorageMode getMode() {
        return mode;
    }

    public void setMode(StorageMode mode) {
        this.mode = mode;
    }

    public AutoMode getAutoMode() {
        return autoMode;
    }

    public void setAutoMode(AutoMode autoMode) {
        this.autoMode = autoMode;
    }

    public HandlerData getBaseData() {
        return baseData;
    }

    public StorageMode nextMode() {
        StorageMode supported = baseData.getSupportedMode();
        StorageMode next = mode.nextNonNone();

        return next != supported && supported != StorageMode.BOTH ? supported : next;
    }
}
