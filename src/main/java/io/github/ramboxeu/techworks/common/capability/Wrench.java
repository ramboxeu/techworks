package io.github.ramboxeu.techworks.common.capability;

import net.minecraft.util.math.BlockPos;

public final class Wrench {
    private BlockPos boilerPos;

    public BlockPos getBoilerPos() {
        return boilerPos;
    }

    public void setBoilerPos(BlockPos boilerPos) {
        this.boilerPos = boilerPos;
    }
}
