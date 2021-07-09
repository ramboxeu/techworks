package io.github.ramboxeu.techworks.common.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class MathUtils {

    public static Direction getDirectionFromPos(BlockPos a, BlockPos b) {
        if (a.equals(b)) {
            return null;
        }

        BlockPos c = a.add(-b.getX(), -b.getY(), -b.getZ());

        int horizontalAngle = (int) Math.toDegrees(Math.atan2(c.getX(), c.getZ()));
        int verticalAngle = (int) Math.toDegrees(Math.atan(c.getY()));

        if (verticalAngle == 0) {
            switch (horizontalAngle) {
                case 0: return Direction.NORTH;
                case 90: return Direction.WEST;
                case -90: return Direction.EAST;
                case 180: return Direction.SOUTH;
            }
        } else {
            switch (verticalAngle) {
                case -45: return Direction.UP;
                case 45: return Direction.DOWN;
            }
        }

        return null;
    }

    public static float calcProgress(int value, int max) {
        if (max <= 0 || value <= 0) {
            return 0;
        }

        float progress = value / (float)max;
        return Math.min(Math.max(0.0f, progress), 1.0f);
    }

}
