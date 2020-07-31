package io.github.ramboxeu.techworks.common.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.HashMap;
import java.util.Map;

public class CapsNames {
    public static final Map<Capability<?>, String> NAMES = new HashMap<>(3);
    public static final Map<String, Capability<?>> CAPS = new HashMap<>(3);

    public static String toName(Capability<?> cap) {
        return cap != null ? NAMES.get(cap) : "none";
    }

    @SuppressWarnings("unchecked")
    public static <T> Capability<T> fromName(String name) {
        return (Capability<T>)CAPS.get(name);
    }

    private static void put(Capability<?> capability, String name) {
        NAMES.put(capability, name);
        CAPS.put(name, capability);
    }

    static {
        put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, "item");
        put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, "fluid");
        put(CapabilityEnergy.ENERGY, "energy");

        CAPS.put("none", null);
    }
}
