package io.github.ramboxeu.techworks.common.machine.io;

import net.minecraft.util.Direction;
/*
    This is somewhat temporary, but must do it for now
 */
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

    public IOManager(Mode[] up , Mode[] down, Mode[] north, Mode[] south, Mode[] east, Mode[] west) {
        modes = new Mode[][] {
                up,
                down,
                north,
                south,
                east,
                west
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
