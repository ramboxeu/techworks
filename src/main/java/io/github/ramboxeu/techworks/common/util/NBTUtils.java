package io.github.ramboxeu.techworks.common.util;

import net.minecraft.nbt.CompoundNBT;

public class NBTUtils {
    public static CableConnections readCableConnections(CompoundNBT nbt) {
        return new CableConnections.Builder()
                .down(nbt.getBoolean("down"))
                .up(nbt.getBoolean("up"))
                .north(nbt.getBoolean("north"))
                .south(nbt.getBoolean("south"))
                .west(nbt.getBoolean("west"))
                .east(nbt.getBoolean("east"))
                .build();
    }

    public static CompoundNBT writeCableConnections(CableConnections connections) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("down", connections.isDown());
        nbt.putBoolean("up", connections.isUp());
        nbt.putBoolean("north", connections.isNorth());
        nbt.putBoolean("south", connections.isSouth());
        nbt.putBoolean("west", connections.isWest());
        nbt.putBoolean("east", connections.isEast());
        return nbt;
    }
}
