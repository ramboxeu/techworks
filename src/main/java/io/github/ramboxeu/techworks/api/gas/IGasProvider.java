package io.github.ramboxeu.techworks.api.gas;

import io.github.ramboxeu.techworks.common.gas.Gas;
import net.minecraft.util.Direction;

public interface IGasProvider extends IGasHandler {
    Gas.Unit extractGas(Direction side, Gas.Unit gasUnit, boolean simulate);
}
