package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.common.machine.io.IOManager;
import io.github.ramboxeu.techworks.common.machine.io.Mode;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.util.capability.EnergyWrapper;
import io.github.ramboxeu.techworks.common.util.capability.FluidWrapper;
import io.github.ramboxeu.techworks.common.util.capability.InventoryWrapper;
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
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private int cooldown;
    private int cooldownCounter = 0;

    protected LazyOptional<IItemHandlerModifiable> inventory = LazyOptional.of(this::createItemHandler);
    protected LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergyStorage);
    protected LazyOptional<IGasHandler> gasHandler = LazyOptional.of(this::createGasHandler);
    protected LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(this::createFluidHandler);

    private IOManager manager;

    public AbstractMachineTile(TileEntityType<?> tileEntityType, int cooldown) {
        super(tileEntityType);
        this.cooldown = cooldown;
        this.cooldownCounter = cooldown;
        this.manager = new IOManager(IOManager.DEFAULT, new Mode[] {Mode.INPUT, Mode.NONE, Mode.NONE, Mode.NONE}, IOManager.DEFAULT, IOManager.DEFAULT, IOManager.DEFAULT, IOManager.DEFAULT);
    }

    @Override
    public void tick() {
        if (cooldownCounter == 0) {
            cooldownCounter = cooldown;
            this.run();
        }

        if (this.isWorking()) {
            this.world.setBlockState(this.pos, this.getBlockState().with(TechworksBlockStateProperties.RUNNING, true));
        }

        cooldownCounter--;
    }

    abstract void run();

    @Nullable
    protected abstract IItemHandlerModifiable createItemHandler();

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
        return this.getCapability(cap, side, null);
    }

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
                if (mode == null)
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
                if (mode == null)
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
                if (mode == null)
                    return LazyOptional.of(() -> new FluidWrapper(this.fluidHandler.orElse(null), canInsert(m), canExtract(m))).cast();
                if (mode == m)
                    return fluidHandler.cast();
            }
        }

        if (capability == CapabilityGas.GAS && gasHandler.isPresent()) {
            if (side == null || mode == manager.getGasHandlerMode(side))
                return gasHandler.cast();
        }

        return super.getCapability(capability, side);
    }

    private boolean canInsert(Mode mode) {
        return mode == Mode.INPUT || mode == Mode.BOTH;
    }

    private boolean canExtract(Mode mode) {
        return mode == Mode.OUTPUT || mode == Mode.BOTH;
    }

    abstract boolean isWorking();
}
