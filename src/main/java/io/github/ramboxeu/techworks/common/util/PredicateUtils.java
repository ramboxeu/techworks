package io.github.ramboxeu.techworks.common.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

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
}
