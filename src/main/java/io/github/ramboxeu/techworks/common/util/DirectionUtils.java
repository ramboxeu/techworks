package io.github.ramboxeu.techworks.common.util;

import net.minecraft.util.Direction;

public class DirectionUtils {

    public static boolean isPositive(Direction direction) {
        return direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;
    }

    public static boolean isNegative(Direction direction) {
        return direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
    }

    public static boolean isHorizontal(Direction direction) {
        return direction.getAxis().isHorizontal();
    }

    public static boolean isVertical(Direction direction) {
        return direction.getAxis().isVertical();
    }

    public static boolean isXAxis(Direction direction) {
        return direction.getAxis() == Direction.Axis.X;
    }

    public static boolean isZAxis(Direction direction) {
        return direction.getAxis() == Direction.Axis.Z;
    }

    public static boolean isYAxis(Direction direction) {
        return direction.getAxis() == Direction.Axis.Y;
    }
}
