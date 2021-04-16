package io.github.ramboxeu.techworks.common.util.machineio.handler;

import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;

import java.util.List;

public interface IContainedHandler {
    StorageMode getMode();
    void setMode(StorageMode mode);
    List<StorageMode> getAvailableModes();
}
