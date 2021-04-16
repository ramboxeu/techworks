package io.github.ramboxeu.techworks.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class PredicateUtils {
    static public boolean isFuel(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack) > 0;
    }

    public static boolean isFluidHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }

    public static boolean isEnergyStorage(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    public static boolean isLiquid(FluidStack stack) {
        return !stack.getFluid().getAttributes().isGaseous(stack);
    }

    public static boolean isGas(FluidStack stack) {
        return stack.getFluid().getAttributes().isGaseous(stack);
    }

    public static boolean isItemHandler(@Nullable Direction side, @Nullable TileEntity tile) {
        return tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent();
    }

    public static boolean isItemHandler(@Nullable TileEntity tile) {
        return isItemHandler(null, tile);
    }

    public static boolean isFluidHandler(@Nullable Direction side, @Nullable TileEntity tile) {
        return tile != null && tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side).isPresent();
    }

    public static boolean isEnergyHandler(@Nullable Direction side, @Nullable TileEntity tile) {
        return tile != null && tile.getCapability(CapabilityEnergy.ENERGY, side).isPresent();
    }
}
