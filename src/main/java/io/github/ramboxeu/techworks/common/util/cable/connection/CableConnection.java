package io.github.ramboxeu.techworks.common.util.cable.connection;

public class CableConnection {
    private ConnectionMode mode;
    private ConnectionStatus status;

    public CableConnection(ConnectionMode mode, ConnectionStatus status) {
        this.mode = mode;
        this.status = status;
    }

    void set(ConnectionMode mode, ConnectionStatus status) {
        this.mode = mode;
        this.status = status;
    }

    void setMode(ConnectionMode mode) {
        this.mode = mode;
    }

    void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public ConnectionMode getMode() {
        return mode;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    static CableConnection newDefault() {
        return new CableConnection(ConnectionMode.BOTH, ConnectionStatus.DISCONNECTED);
    }
}
