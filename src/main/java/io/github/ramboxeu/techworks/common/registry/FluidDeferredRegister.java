package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class FluidDeferredRegister {
    private final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Techworks.MOD_ID);

    public <T extends Fluid> FluidRegistryObject<T> register(String name, Supplier<T> supplier) {
        return new FluidRegistryObject<>(FLUIDS.register(name, supplier));
    }

    public void register(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
