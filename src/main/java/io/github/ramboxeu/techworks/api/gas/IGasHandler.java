package io.github.ramboxeu.techworks.api.gas;

import io.github.ramboxeu.techworks.common.gas.Gas;

public interface IGasHandler {
    Gas getGas();
    int getMaxPressure();
}
