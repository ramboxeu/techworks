package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.util.machineio.InputType;

public abstract class HandlerData {
    protected final InputType type;
    protected final int color;
    protected final int identity;
    protected final Object object;

    public HandlerData(InputType type, int color, int identity, Object object) {
        this.type = type;
        this.color = color;
        this.identity = identity;
        this.object = object;
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
}
