package io.github.ramboxeu.techworks.common.util.machineio.handler;

import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;

import java.util.List;

/**
 * Interface for handlers which act as container for other handlers (that actually store data)
 */
public interface IHandlerContainer {
    HandlerConfig remove(HandlerData data);
    void setStorageMode(HandlerData data, StorageMode mode);
    List<HandlerConfig> getConfigs();
}
