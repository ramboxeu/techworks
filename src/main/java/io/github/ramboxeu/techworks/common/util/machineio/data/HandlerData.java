package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;

public abstract class HandlerData {
    protected final InputType type;
    protected final int color;
    protected final int identity;
    protected final Object object;
    protected final StorageMode supportedMode;

    public HandlerData(InputType type, int color, int identity, Object object, StorageMode supportedMode) {
        this.type = type;
        this.color = color;
        this.identity = identity;
        this.object = object;
        this.supportedMode = supportedMode;
    }

    public InputType getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public Object getObject() {
        return object;
    }

    public int getIdentity() {
        return identity;
    }

    public StorageMode getSupportedMode() {
        return supportedMode;
    }

    public boolean canAutoPush() {
        return supportedMode.canOutput();
    }

    public boolean canAutoPull() {
        return supportedMode.canInput();
    }
}
