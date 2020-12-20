package io.github.ramboxeu.techworks.common.capability.impl;

import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class DevFluidHandler implements IFluidHandler, INBTSerializable<CompoundNBT> {
    private final Predicate<FluidStack> predicate;
    private final DevBlockTile tile;
    private Fluid fluid = Fluids.EMPTY;

    public DevFluidHandler(DevBlockTile tile, Predicate<FluidStack> predicate) {
        this.tile = tile;
        this.predicate = predicate;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(fluid, Integer.MAX_VALUE);
    }

    @Override
    public int getTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return predicate.test(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        Fluid fluid = resource.getFluid();
        boolean isGaseous = fluid.getAttributes().isGaseous(resource);

        tile.createLog((isGaseous ? "G " : "L ") + "R " + (action.simulate() ? "sim " : "exe ") + fluid.getRegistryName() + " " + resource.getAmount());
        return resource.getAmount();
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        Fluid fluid = resource.getFluid();
        boolean isGaseous = fluid.getAttributes().isGaseous(resource);

        if (fluid != this.fluid) {
            tile.createLog((isGaseous ? "G " : "L ") + "E " + (action.simulate() ? "sim " : "exe ") + "E (" + fluid.getRegistryName() + " " + resource.getAmount() + ")");
            return FluidStack.EMPTY;
        }

        tile.createLog((isGaseous ? "G " : "L ") + "E " + (action.simulate() ? "sim " : "exe ") + fluid.getRegistryName() + " " + resource.getAmount());
        return resource;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return new FluidStack(fluid, maxDrain);
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid != null ? fluid : Fluids.EMPTY;
    }

    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("RegId", getFluidRegistryName());
        return tag;
    }

    public String getFluidRegistryName() {
        ResourceLocation emptyLocation = new ResourceLocation("minecraft:empty");

        if (fluid != null) {
            if (fluid.getRegistryName() != null) {
                return fluid.getRegistryName().toString();
            }
        }

        return emptyLocation.toString();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ResourceLocation fluidId = new ResourceLocation(nbt.getString("RegId"));
        fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
    }
}
