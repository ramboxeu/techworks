package io.github.ramboxeu.techworks.common.util.machineio;

public enum StorageMode {
    NONE,
    INPUT,
    OUTPUT,
    BOTH;

    public boolean canInput() {
        return this == INPUT || this == BOTH;
    }

    public boolean canOutput() {
        return this == OUTPUT || this == BOTH;
    }

    public StorageMode next() {
        switch (this) {
            case INPUT:
                return OUTPUT;
            case OUTPUT:
                return BOTH;
            case BOTH:
                return NONE;
            case NONE:
                return INPUT;
            default:
                return null;
        }
    }

    public StorageMode nextNonNone() {
        switch (this) {
            case INPUT:
                return OUTPUT;
            case OUTPUT:
                return BOTH;
            default:
                return INPUT;
        }
    }
}
