package io.github.ramboxeu.techworks.common.util.machineio;

import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;

@FunctionalInterface
public interface IDataAdder<T extends HandlerData> {
    void add(Side side, T data, StorageMode mode, AutoMode auto);
}
