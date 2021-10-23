package io.github.ramboxeu.techworks.common.util;

public enum StandbyMode {
    ON,
    OFF;

    public boolean canWork() {
        return this == OFF;
    }

    public StandbyMode next() {
        switch (this) {
            case ON:
                return OFF;
            case OFF:
                return ON;
        }

        throw new AssertionError();
    }
}
