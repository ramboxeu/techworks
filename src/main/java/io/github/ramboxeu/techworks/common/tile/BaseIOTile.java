package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.tile.machine.MachineIO;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public abstract class BaseIOTile extends BaseTechworksTile {
    protected MachineIO machineIO;

    public BaseIOTile(TileEntityType<?> type) {
        super(type);
        machineIO = MachineIO.DISABLED;
    }

    @Nonnull
    public MachineIO getMachineIO() {
        return machineIO;
    }
}
