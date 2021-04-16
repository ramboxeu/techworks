package io.github.ramboxeu.techworks.common.util;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class DevUtils {
    public static String getCapName(Capability<?> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return "Item";
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return "Fluid";
        } else if (cap == CapabilityEnergy.ENERGY) {
            return "Energy";
        }

        return "";
    }

    public static String fluidStackToString(FluidStack stack) {
        return stack.getFluid().getRegistryName().toString() + " " + stack.getAmount();
    }
}
