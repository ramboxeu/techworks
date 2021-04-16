package io.github.ramboxeu.techworks.common.util.cable.connection;

public enum ConnectionMode {
    BOTH,
    INPUT,
    OUTPUT,
    CONNECTION;

    public boolean canInput() {
        return this == BOTH || this == OUTPUT;
    }

    public boolean canOutput() {
        return this == BOTH || this == INPUT;
    }

    public boolean isConnection() {
        return this == CONNECTION;
    }

    public boolean isInput() {
        return this == INPUT;
    }

    public boolean isOutput() {
        return this == OUTPUT;
    }

    public ConnectionMode orBoth() {
        return this == CONNECTION ? BOTH : this;
    }

    public ConnectionMode next() {
        switch (this) {
            case BOTH:
                return INPUT;
            case INPUT:
                return OUTPUT;
            default:
                return BOTH;
        }
    }
}
