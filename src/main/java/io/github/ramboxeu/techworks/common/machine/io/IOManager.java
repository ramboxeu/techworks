package io.github.ramboxeu.techworks.common.machine.io;

import net.minecraft.util.Direction;

public class IOManager {
    // 0 - item, 1 - energy, 3 - gas, 4 - fluid
    private Mode[][] modes;

    public static final Mode[] DEFAULT = new Mode[] {Mode.NONE, Mode.NONE, Mode.NONE, Mode.NONE};

    public IOManager() {
        modes = new Mode[][]{
            DEFAULT,
            DEFAULT,
            DEFAULT,
            DEFAULT,
            DEFAULT,
            DEFAULT,
        };
    }

    public IOManager(Mode[] item , Mode[] energy, Mode[] gas, Mode[] fluid) {
        modes = new Mode[][] {
                item,
                energy,
                gas,
                fluid,
        };
    }

    public Mode getItemHandlerMode(Direction side) {
        return modes[side.getIndex()][0];
    }

    public Mode getEnergyHandlerMode(Direction side) {
        return modes[side.getIndex()][1];
    }

    public Mode getGasHandlerMode(Direction side) {
        return modes[side.getIndex()][2];
    }

    public Mode getFluidHandlerMode(Direction side) {
        return modes[side.getIndex()][3];
    }
}
