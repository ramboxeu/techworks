package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
    protected LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(this::createFluidHandler);

    private boolean[] itemHandlerSides   = {false, false, false, false, false, false};
    private boolean[] energyHandlerSides = {false, false, false, false, false, false};
    private boolean[] gasHandlerSides    = {false, false, false, false, false, false};
    private boolean[] fluidHandlerSides  = {false, false, false, false, false, false};

    public AbstractMachineTile(TileEntityType<?> tileEntityType, int cooldown) {
        super(tileEntityType);
        this.cooldown = cooldown;
        this.cooldownCounter = cooldown;
    }

    @Override
    public void tick() {
        if (cooldownCounter == 0) {
            cooldownCounter = cooldown;
            this.run();
        }

        cooldownCounter--;
    }

    abstract void run();

    @Nullable
    protected abstract IItemHandler createItemHandler();

    @Nullable
    protected abstract IEnergyStorage createEnergyStorage();

    @Nullable
    protected abstract IGasHandler createGasHandler();

    @Nullable
    protected abstract IFluidHandler createFluidHandler();


    @Override
    public void read(CompoundNBT compound) {
        this.inventory.ifPresent(itemHandler -> ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(compound.getCompound("Inventory")));
        this.energyStorage.ifPresent(energyStorage -> ((INBTSerializable<CompoundNBT>) energyStorage).deserializeNBT(compound.getCompound("Energy")));
        this.gasHandler.ifPresent(gasHandler -> CapabilityGas.GAS.readNBT(gasHandler, null, compound.getCompound("GasHandler")));

        super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.inventory.ifPresent(itemHandler -> compound.put("Inventory", ((INBTSerializable<CompoundNBT>) itemHandler).serializeNBT()));
        this.energyStorage.ifPresent(energyStorage -> compound.put("Energy", ((INBTSerializable<CompoundNBT>) energyStorage).serializeNBT()));
        this.gasHandler.ifPresent(gasHandler -> compound.put("GasHandler", CapabilityGas.GAS.writeNBT(gasHandler, null)));

        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null || itemHandlerSides[side.getIndex()])
                return inventory.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            if (side == null || energyHandlerSides[side.getIndex()])
                return energyStorage.cast();
        }
        if (cap == CapabilityGas.GAS) {
            if (side == null || gasHandlerSides[side.getIndex()])
                return gasHandler.cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (side == null || fluidHandlerSides[side.getIndex()]) {
                return fluidHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }
}
