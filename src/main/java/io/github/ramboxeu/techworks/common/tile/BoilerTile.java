package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class BoilerTile extends AbstractMachineTile {
    public BoilerTile() {
        super(Registration.BOILER_TILE.get(), 10);
    }

    @Override
    void run() {}

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

    @Nullable
    @Override
    protected IItemHandlerModifiable createItemHandler() {
        return new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    @Nullable
    @Override
    protected IEnergyStorage createEnergyStorage() {
        return null;
    }

    @Nullable
    @Override
    protected IGasHandler createGasHandler() {
        return new GasHandler(Registration.STEAM_GAS.get(), 100, 1000) {
            @Override
            public void onContentsChanged() {
                markDirty();
            }
        };
    }

    @Nullable
    @Override
    protected IFluidHandler createFluidHandler() {
        return new FluidTank(1000, fluidStack -> fluidStack.getFluid().equals(Fluids.WATER));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.boiler");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BoilerContainer(id, playerInventory, this);
    }
}
