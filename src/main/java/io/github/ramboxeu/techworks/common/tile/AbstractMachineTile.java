package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private int cooldown;
    private int cooldownCounter = 0;

    protected LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createItemHandler);
    protected LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergyStorage);
    protected LazyOptional<IGasHandler> gasHandler = LazyOptional.of(this::createGasHandler);

    public AbstractMachineTile(TileEntityType<?> tileEntityType, int cooldown) {
        super(tileEntityType);
        this.cooldown = cooldown;
        this.cooldownCounter = cooldown;
    }

    @Override
    public void tick() {
        if (cooldownCounter <= 0) {
            cooldownCounter = cooldown;
        }

        run();

        cooldownCounter--;
    }

    abstract void run();

    @Nullable
    protected abstract IItemHandler createItemHandler();

    @Nullable
    protected abstract IEnergyStorage createEnergyStorage();

    @Nullable
    protected abstract IGasHandler createGasHandler();


    @Override
    public void read(CompoundNBT compound) {
        this.inventory.ifPresent(itemHandler -> ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(compound.getCompound("Inventory")));
        this.energyStorage.ifPresent(energyStorage -> ((INBTSerializable<CompoundNBT>) energyStorage).deserializeNBT(compound.getCompound("Energy")));

        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.inventory.ifPresent(itemHandler -> compound.put("Inventory", ((INBTSerializable<CompoundNBT>) itemHandler).serializeNBT()));
        this.energyStorage.ifPresent(energyStorage -> compound.put("Energy", ((INBTSerializable<CompoundNBT>) energyStorage).serializeNBT()));

        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }
}
