package io.github.ramboxeu.techworks.common.util;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.util.Direction;

import java.util.HashMap;
import java.util.Map;

public class CableConnections {
    private boolean north;
    private boolean south;
    private boolean west;
    private boolean east;
    private boolean up;
    private boolean down;

    public CableConnections(boolean north, boolean south, boolean west, boolean east, boolean up, boolean down) {
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
        this.up = up;
        this.down = down;
    }

    public boolean isNorth() {
        return north;
    }

    public boolean isSouth() {
        return south;
    }

    public boolean isWest() {
        return west;
    }

    public boolean isEast() {
        return east;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public void dump() {
        Techworks.LOGGER.debug("Up: {} Down: {} North: {} South: {} West: {} East: {}", up, down, north, south, west, east);
    }

    public boolean[] getAsArray() {
        return new boolean[]{down, up, north, south, west, south};
    }

    public Map<Direction, Boolean> getAsMap() {
        return new HashMap<Direction, Boolean>() {
            {
                put(Direction.DOWN, down);
                put(Direction.UP, up);
                put(Direction.NORTH, north);
                put(Direction.SOUTH, south);
                put(Direction.WEST, west);
                put(Direction.EAST, east);
            }
        };
    }

    public static class Builder {
        private boolean[] connections = new boolean[6];

        public Builder() {}

        public Builder(CableConnections connections) {
            this.connections[0] = connections.isDown();
            this.connections[1] = connections.isUp();
            this.connections[2] = connections.isNorth();
            this.connections[3] = connections.isSouth();
            this.connections[4] = connections.isWest();
            this.connections[5] = connections.isNorth();
        }

        public Builder up(boolean up) {
            return setDirection(up, Direction.UP);
        }

        public Builder down(boolean down) {
            return setDirection(down, Direction.DOWN);
        }

        public Builder north(boolean north) {
            return setDirection(north, Direction.NORTH);
        }

        public Builder south(boolean south) {
            return setDirection(south, Direction.SOUTH);
        }

        public Builder west(boolean west) {
            return setDirection(west, Direction.WEST);
        }

        public Builder east(boolean east) {
            return setDirection(east, Direction.EAST);
        }

        public Builder setDirection(boolean b, Direction direction) {
            connections[direction.getIndex()] = b;
            return this;
        }

        public CableConnections build() {
            return new CableConnections(connections[2], connections[3], connections[4], connections[5], connections[1], connections[0]);
        }
    }
}
