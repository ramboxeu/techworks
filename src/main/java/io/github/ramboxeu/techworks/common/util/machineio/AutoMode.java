package io.github.ramboxeu.techworks.common.util.machineio;

// InputAutomatization????
// AutoInput
public enum AutoMode {
    OFF,
    PUSH,
    PULL,
    BOTH;

    public boolean isPush() {
        return this == PUSH || this == BOTH;
    }

    public boolean isPull() {
        return this == PULL || this == BOTH;
    }

    public AutoMode notPush() {
        return this == PUSH || this == OFF ? OFF : PULL;
    }

    public AutoMode notPull() {
        return this == PULL || this == OFF ? OFF : PUSH;
    }

    public AutoMode andPush() {
        return this == OFF || this == PUSH ? PUSH : BOTH;
    }

    public AutoMode andPull() {
        return this == OFF || this == PULL ? PULL : BOTH;
    }

    public AutoMode togglePush(boolean push) {
        return push ? andPush() : notPush();
    }

    public AutoMode togglePull(boolean pull) {
        return pull ? andPull() : notPull();
    }
}
