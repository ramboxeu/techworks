package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.MachinePort;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseIOTile extends BaseTechworksTile implements IWrenchable {
    protected MachineIO machineIO;

    public BaseIOTile(TileEntityType<?> type) {
        super(type);
        machineIO = new MachineIO(this::getFacing, this::getFallbackCapability);
    }

    public MachineIO getMachineIO() {
        return machineIO;
    }

    @Nullable
    protected <T> LazyOptional<T> getFallbackCapability(@Nonnull Capability<T> capability, Side side) {
        return null;
    }

    @Nonnull
    public Direction getFacing() {
        return Direction.NORTH;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction face) {
        return machineIO.getTileCapability(cap, face, this.getCapabilities());
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        machineIO.invalidateCaps();
    }

    @Override
    public boolean configure(World world, BlockPos pos, Direction face, Vector3d hitVec) {
        MachinePort port = machineIO.getPort(face);

        Techworks.LOGGER.debug("Disabled: {}", port.isDisabled());

        return true;
    }
}
