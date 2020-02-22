package io.github.ramboxeu.techworks.api.gas;

public interface IGasStorage extends IGasHandler, IGasProvider, IGasReceiver {
    int getGasAmountStored();
    int getMaxGasAmountStored();
}
