package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.common.capability.extensions.IInventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.machine.io.IOManager;
import io.github.ramboxeu.techworks.common.machine.io.Mode;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.util.capability.EnergyWrapper;
import io.github.ramboxeu.techworks.common.util.capability.FluidWrapper;
import io.github.ramboxeu.techworks.common.util.capability.GasWrapper;
import io.github.ramboxeu.techworks.common.util.capability.InventoryWrapper;
import net.minecraft.block.BlockState;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private int operationTime;
    private int timeCounter = 0;

    protected LazyOptional<IInventoryItemStackHandler> inventory;
    protected LazyOptional<IEnergyStorage> energyStorage;
    protected LazyOptional<IGasHandler> gasHandler;
    protected LazyOptional<IFluidHandler> fluidHandler;
    protected int speedMultiplier = 1;

    private IOManager manager;

    public AbstractMachineTile(TileEntityType<?> tileEntityType, int operationTime) {
        super(tileEntityType);
        this.operationTime = operationTime;
        this.manager = new IOManager(
                new Mode[] {Mode.BOTH, Mode.BOTH, Mode.BOTH, Mode.BOTH},
                new Mode[] {Mode.BOTH, Mode.BOTH, Mode.BOTH, Mode.BOTH},
                new Mode[] {Mode.BOTH, Mode.BOTH, Mode.BOTH, Mode.BOTH},
                new Mode[] {Mode.BOTH, Mode.BOTH, Mode.BOTH, Mode.BOTH},
                new Mode[] {Mode.BOTH, Mode.BOTH, Mode.BOTH, Mode.BOTH},
                new Mode[] {Mode.BOTH, Mode.BOTH, Mode.BOTH, Mode.BOTH});

        if (hasItemHandler()) {
            inventory = LazyOptional.of(this::createItemHandler);
        } else {
            inventory = LazyOptional.empty();
        }

        if (hasEnergyStorage()) {
            energyStorage = LazyOptional.of(this::createEnergyStorage);
        } else {
            energyStorage = LazyOptional.empty();
        }

        if (hasGasHandler()) {
            gasHandler = LazyOptional.of(this::createGasHandler);
        } else {
            gasHandler = LazyOptional.empty();
        }

        if (hasFluidHandler()) {
            fluidHandler = LazyOptional.of(this::createFluidHandler);
        } else {
            fluidHandler = LazyOptional.empty();
        }
    }

    @Override
    public void tick() {
        if (this.canWork()) {
            this.timeCounter++;
        } else {
            timeCounter = 0;
        }

        if (!this.world.isRemote) {
            if (this.canWork()) {
                this.world.setBlockState(this.pos, this.getBlockState().with(TechworksBlockStateProperties.RUNNING, Boolean.valueOf(true)));
            } else {
                this.world.setBlockState(this.pos, this.getBlockState().with(TechworksBlockStateProperties.RUNNING, Boolean.valueOf(false)));
            }

            if (this.timeCounter == this.operationTime) {
                this.run();
                this.timeCounter = 0;
            }
        }
    }

    abstract void run();

    protected void setOperationTime(int operationTime){
        this.operationTime = operationTime / speedMultiplier;
    }

    protected IInventoryItemStackHandler createItemHandler() {
        return null;
    }

    protected IEnergyStorage createEnergyStorage() {
        return null;
    }

    protected IGasHandler createGasHandler() {
        return null;
    }

    protected IFluidHandler createFluidHandler() {
        return null;
    }

    public boolean hasItemHandler() {
        return false;
    }

    public boolean hasEnergyStorage() {
        return false;
    }

    public boolean hasGasHandler() {
        return false;
    }

    public boolean hasFluidHandler() {
        return false;
    }

//    @Override
//    @SuppressWarnings("unchecked")
//    public void read(CompoundNBT compound) {
//        this.inventory.ifPresent(itemHandler -> ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(compound.getCompound("Inventory")));
//        this.energyStorage.ifPresent(energyStorage -> CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.get("Energy")));
//        this.fluidHandler.ifPresent(fluidHandler -> CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidHandler, null, compound.getCompound("FluidTank")));
//        this.gasHandler.ifPresent(gasHandler -> CapabilityGas.GAS.readNBT(gasHandler, null, compound.getCompound("GasHandler")));
//        this.timeCounter = compound.getInt("counter");
//
//        super.read(compound);
//    }


    @SuppressWarnings("unchecked")
    @Override
    // a.k.a read from NBT
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        this.inventory.ifPresent(itemHandler -> ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(compound.getCompound("Inventory")));
        this.energyStorage.ifPresent(energyStorage -> CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.get("Energy")));
        this.fluidHandler.ifPresent(fluidHandler -> CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidHandler, null, compound.getCompound("FluidTank")));
        this.gasHandler.ifPresent(gasHandler -> CapabilityGas.GAS.readNBT(gasHandler, null, compound.getCompound("GasHandler")));
        this.timeCounter = compound.getInt("counter");
        super.func_230337_a_(state, compound);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompoundNBT write(CompoundNBT compound) {
        this.inventory.ifPresent(itemHandler -> compound.put("Inventory", ((INBTSerializable<CompoundNBT>) itemHandler).serializeNBT()));
        this.energyStorage.ifPresent(energyStorage -> compound.put("Energy", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null)));
        this.fluidHandler.ifPresent(fluidHandler -> compound.put("FluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidHandler, null)));
        this.gasHandler.ifPresent(gasHandler -> compound.put("GasHandler", CapabilityGas.GAS.writeNBT(gasHandler, null)));
        compound.putInt("counter", timeCounter);

        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap, side, null);
    }

    // TODO: Remove
    /*
        Null mode assumes that capability is requested for Vanilla-like behaviour and will be wrapped to prevent unwanted behaviour, eg. inputting to output
    */
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side, @Nullable Mode mode) {
        if (mode == Mode.NONE)
            return LazyOptional.empty();

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventory.isPresent()) {
            if (side == null)
                return inventory.cast();
            else {
                Mode m = manager.getItemHandlerMode(side);
                if (mode == null && m != Mode.NONE)
                    return LazyOptional.of(() -> new InventoryWrapper(this.inventory.orElse(null), canInsert(m), canExtract(m))).cast();
                if (mode == m)
                    return inventory.cast();
            }
        }

        if (capability == CapabilityEnergy.ENERGY && energyStorage.isPresent()) {
            if (side == null)
                return energyStorage.cast();
            else {
                Mode m = manager.getEnergyHandlerMode(side);
                if (mode == null && m != Mode.NONE)
                    return LazyOptional.of(() -> new EnergyWrapper(this.energyStorage.orElse(null), canInsert(m), canExtract(m))).cast();
                if (mode == m)
                    return energyStorage.cast();
            }
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && fluidHandler.isPresent()) {
            if (side == null)
                return fluidHandler.cast();
            else {
                Mode m = manager.getFluidHandlerMode(side);
                if (mode == null && m != Mode.NONE)
                    return LazyOptional.of(() -> new FluidWrapper(this.fluidHandler.orElse(null), canInsert(m), canExtract(m))).cast();
                if (mode == m)
                    return fluidHandler.cast();
            }
        }

        if (capability == CapabilityGas.GAS && gasHandler.isPresent()) {
            if (side == null)
                return gasHandler.cast();
            else {
                Mode m = manager.getGasHandlerMode(side);
                if (mode == null && m != Mode.NONE)
                    return LazyOptional.of(() -> new GasWrapper(this.gasHandler.orElse(null), canInsert(m), canExtract(m))).cast();
                if (mode == m)
                    return gasHandler.cast();
            }
        }

        return super.getCapability(capability, side);
    }

    private boolean canInsert(Mode mode) {
        return mode == Mode.INPUT || mode == Mode.BOTH;
    }

    private boolean canExtract(Mode mode) {
        return mode == Mode.OUTPUT || mode == Mode.BOTH;
    }

    abstract boolean canWork();

    public int getOperationTime() {
        return this.operationTime;
    }

    public int getTimeCounter() {
        return this.timeCounter;
    }
}
