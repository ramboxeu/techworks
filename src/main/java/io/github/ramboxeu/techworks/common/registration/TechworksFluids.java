package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.fluid.SteamGas;
import io.github.ramboxeu.techworks.common.registry.FluidDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.FluidRegistryObject;

public class TechworksFluids {
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister();

    public static final FluidRegistryObject<SteamGas> STEAM = FLUIDS.register("steam", SteamGas::new);
}
