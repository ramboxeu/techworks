package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.component.EnergyStorageComponent;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.energy.EnergyStorageWrapper;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStorageTile extends StorageTile<EnergyStorageComponent> {
    private final EnergyBattery battery;
    private final LazyOptional<IEnergyStorage>[] holders;

    public EnergyStorageTile() {
        super(TechworksTiles.ENERGY_STORAGE.get(), TechworksComponents.ENERGY_STORAGE.get());

        battery = new EnergyBattery(20000, 100) {
            @Override
            protected void onContentsChanged() {
                syncContents();
            }

            @Override
            public void onComponentsChanged(EnergyStorageComponent component, ItemStack stack) {
                capacity = component.getStorageCapacity();
                energy = Math.min(energy, capacity);
                maxExtract = component.getStorageTransfer();
                maxReceive = component.getStorageTransfer();
            }
        };
        battery.onComponentsChanged(component, componentStack);
        holders = new LazyOptional[]{ LazyOptional.of(() -> new EnergyStorageWrapper(battery)), LazyOptional.of(() -> new EnergyStorageWrapper(battery)), LazyOptional.of(() -> new EnergyStorageWrapper(battery)), LazyOptional.of(() -> new EnergyStorageWrapper(battery)), LazyOptional.of(() -> new EnergyStorageWrapper(battery)), LazyOptional.of(() -> new EnergyStorageWrapper(battery)), LazyOptional.of(() -> battery) };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side != null)
            return CapabilityEnergy.ENERGY.orEmpty(cap, holders[side.getIndex()]);

        return CapabilityEnergy.ENERGY.orEmpty(cap, holders[6]);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("Battery", battery.serializeNBT());
        tag.put("SideConfig", writeSideConfig());
        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT tag, BlockState state) {
        super.readUpdateTag(tag, state);
        battery.deserializeNBT(tag.getCompound("Battery"));
        readSideConfig(tag.getList("SideConfig", Constants.NBT.TAG_STRING));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Battery", battery.serializeNBT());
        tag.put("SideConfig", writeSideConfig());
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        battery.deserializeNBT(tag.getCompound("Battery"));
        readSideConfig(tag.getList("SideConfig", Constants.NBT.TAG_STRING));
    }

    private void readSideConfig(ListNBT tag) {
        for (int i = 0; i < 6; i++) {
            StorageMode mode = StorageMode.valueOf(tag.getString(i));
            ((EnergyStorageWrapper) Utils.unpack(holders[i])).setMode(mode);
        }
    }

    private ListNBT writeSideConfig() {
        ListNBT tag = new ListNBT();
        for (int i = 0; i < 6; i++) {
            StorageMode mode = ((EnergyStorageWrapper) Utils.unpack(holders[i])).getMode();
            tag.add(i, StringNBT.valueOf(mode.name()));
        }

        return tag;
    }

    @Override
    public boolean configure(PlayerEntity player, ItemStack wrench, World world, BlockPos pos, Direction face, Vector3d hitVec) {
        if (face.getAxis() == Direction.Axis.X) face = face.getOpposite();

        EnergyStorageWrapper wrapper = (EnergyStorageWrapper) Utils.unpack(holders[face.getIndex()]);
        wrapper.setMode(wrapper.getMode().next());
        return true;
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();

        for (LazyOptional<IEnergyStorage> holder : holders)
            holder.invalidate();
    }

    @Override
    protected void onComponentsChanged(EnergyStorageComponent component, ItemStack stack) {
        battery.onComponentsChanged(component, stack);
    }

    @Override
    protected int getAmountStored() {
        return battery.getEnergyStored();
    }

    public void setContents(int energy) {
        battery.setEnergy(energy);
    }

    public float getFillPercentage() {
        return battery.getEnergyStored() / (float)battery.getMaxEnergyStored();
    }

    public StorageMode getSideStorageMode(Direction side) {
        return ((EnergyStorageWrapper) Utils.unpack(holders[side.getIndex()])).getMode();
    }

    private void syncContents() {
        TechworksPacketHandler.syncEnergyStorage(world.getChunkAt(pos), pos, battery.getEnergyStored());
    }
}
