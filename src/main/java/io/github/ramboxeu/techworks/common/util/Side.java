package io.github.ramboxeu.techworks.common.util;

import net.minecraft.util.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Similar to {@link net.minecraft.util.Direction}, but relative to something
 * <p />
 * Works only for <b>horizontal</b> facing
 */
public enum Side {
    INTERNAL(0, 0),
    BOTTOM(1, 1),
    TOP(2, 2),
    FRONT(3, 4),
    RIGHT(4, 6),
    BACK(5, 8),
    LEFT(6, 10);

    private final int index;
    private final int binIndex;

    Side(int index, int binIndex) {
        this.index = index;
        this.binIndex = binIndex;
    }

    /**
     * Coverts side of <i>something</i> into direction relative to that <i>something</i>
     * @param facing <b>horizontal</b> facing of the thing
     * @return direction which given side corresponds to
     */
    @Nullable
    public Direction toDirection(Direction facing) {
        switch (this) {
            case FRONT:
                return facing;
            case LEFT:
                return facing.rotateYCCW();
            case RIGHT:
                return facing.rotateY();
            case BACK:
                return facing.getOpposite();
            case TOP:
                return Direction.UP;
            case BOTTOM:
                return Direction.DOWN;
            case INTERNAL:
                return null;
        }

        return null;
    }

    public int getIndex() {
        return index;
    }

    public int getBinIndex() {
        return binIndex;
    }

    /**
     * Convert a direction to side of <i>something</i>
     * @param direction direction to convert
     * @param facing <b>horizontal</b> facing fo the thing
     * @return side relative to the thing
     */
    @NotNull
    public static Side fromDirection(Direction direction, Direction facing) {
        if (direction == null) {
            return INTERNAL;
        } else if (direction == facing) {
            return FRONT;
        } else if (direction == Direction.DOWN) {
            return BOTTOM;
        } else if (direction == Direction.UP) {
            return TOP;
        } else if (direction.rotateY() == facing) {
            return LEFT;
        } else if (direction.rotateYCCW() == facing) {
            return RIGHT;
        } else if (direction.getOpposite() == facing) {
            return BACK;
        }

        return null;
    }

    private static final Side[] EXTERNALS = new Side[]{
            BOTTOM,
            TOP,
            FRONT,
            RIGHT,
            BACK,
            LEFT
    };

    public static Side[] external() {
        return EXTERNALS;
    }
}
