package io.github.ramboxeu.techworks.common.util.cable.connection;

public enum ConnectionStatus {
    DISCONNECTED,
    CONNECTED,
    BLOCKED;

    public boolean isBlocked() {
        return this == BLOCKED;
    }

    public boolean isConnected() {
        return this == CONNECTED;
    }
}
