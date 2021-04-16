package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreativeEnergyBatteryTile extends BaseIOTile implements IEnergyStorage {
    private final LazyOptional<IEnergyStorage> holder;

    public CreativeEnergyBatteryTile() {
        super(TechworksTiles.CREATIVE_ENERGY_BATTERY.getTileType());

        holder = LazyOptional.of(() -> this);
    }

    @Nullable
    @Override
    protected <T> LazyOptional<T> getFallbackCapability(@NotNull Capability<T> capability, Side side) {
        return CapabilityEnergy.ENERGY.orEmpty(capability, holder);
    }

    @Override
    protected void serverTick() {
        super.serverTick();

        if (world.isBlockPowered(pos)) {
            for (Direction side : Direction.values()) {
                TileEntity te = world.getTileEntity(pos.offset(side));
                if (te != null) {
                    te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).ifPresent(this::distributeEnergy);
                }
            }
        }
    }

    private void distributeEnergy(IEnergyStorage handler) {
        handler.receiveEnergy(100, false);
    }

    // IEnergyStorage
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int getEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
