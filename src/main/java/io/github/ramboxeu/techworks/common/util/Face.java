package io.github.ramboxeu.techworks.common.util;

import net.minecraft.util.math.vector.Vector3d;

public class Face {
    public final Vector3d leftUp;
    public final Vector3d leftDown;
    public final Vector3d rightUp;
    public final Vector3d rightDown;

    public Face(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        leftDown = new Vector3d(x1, y1, z1);
        leftUp = new Vector3d(x2, y2, z2);
        rightUp = new Vector3d(x3, y3, z3);
        rightDown = new Vector3d(x4, y4, z4);
    }

    public static Face commonX(double x, double y1, double z1, double y2, double z2) {
        return new Face(x, y1, z2, x, y2, z2, x, y2, z1, x, y1, z1);
    }

    public static Face commonY(double y, double x1, double z1, double x2, double z2) {
        return new Face(x1, y, z2, x2, y, z2, x2, y, z1, x1, y, z1);
    }

    public static Face commonZ(double z, double x1, double y1, double x2, double y2) {
        return new Face(x1, y2, z,   x2, y2, z,   x2, y1, z,   x1, y1, z);
    }
}
