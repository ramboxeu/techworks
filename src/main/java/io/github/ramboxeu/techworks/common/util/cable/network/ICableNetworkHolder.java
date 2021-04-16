package io.github.ramboxeu.techworks.common.util.cable.network;

import java.util.UUID;

public interface ICableNetworkHolder {
    BaseCableNetwork getNetwork();
    UUID getId();
    NetworkType getType();
    void update(UUID id, ICableNetwork network);

    @FunctionalInterface
    interface Factory {
        ICableNetworkHolder create(ICableNetwork network, UUID id);
    }
}
