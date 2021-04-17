package io.github.ramboxeu.techworks.common.util.model;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class Cuboid {
    public final double x1;
    public final double y1;
    public final double z1;

    public final double x2;
    public final double y2;
    public final double z2;

    public final double width;
    public final double height;
    public final double depth;

    public final Face down;
    public final Face up;
    public final Face north;
    public final Face south;
    public final Face west;
    public final Face east;

    private final VoxelShape voxelShape;

    public Cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        width = x2 - x1;
        height = y2 - y1;
        depth = z2 - z1;

        down = Face.commonY(y2, x1, z1, x2, z2);
        up = Face.commonY(y1, x1, z1, x2, z2);
        north = Face.commonZ(z2, x1, y1, x2, y2);
        south = Face.commonZ(z1, x1, y1, x2, y2);
        west = Face.commonX(x2, y1, z1, y2, z2);
        east = Face.commonX(x1, y1, z1, y2, z2);

        voxelShape = VoxelShapes.create(x1, y1, z1, x2, y2, z2);
    }

    public VoxelShape getVoxelShape() {
        return voxelShape;
    }

    @Nonnull
    public Face getFace(Direction direction) {
        switch (direction) {
            case DOWN: return down;
            case UP: return up;
            case NORTH: return north;
            case SOUTH: return south;
            case WEST: return west;
            case EAST: return east;
        }

        return null;
    }

    public boolean isVectorWithin(Vector3d vec) {
        return vec.x >= x1 && vec.x <= x2 && vec.y >= y1 && vec.y <= y2 && vec.z >= z1 && vec.z <= z2;
    }

    public static Cuboid make(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new Cuboid(x1 / 16.0, y1 / 16.0, z1 / 16.0, x2 / 16.0, y2 / 16.0, z2 / 16.0);
    }

    public static Cuboid make(int x1, int y1, int z1, int x2, int y2, int z2) {
        return new Cuboid(x1 / 16.0, y1 / 16.0, z1 / 16.0, x2 / 16.0, y2 / 16.0, z2 / 16.0);
    }

}
