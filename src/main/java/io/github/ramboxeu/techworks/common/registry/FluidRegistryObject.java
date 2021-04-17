package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public final class FluidRegistryObject<T extends Fluid> {
    private final RegistryObject<T> fluid;

    public FluidRegistryObject(RegistryObject<T> fluid) {
        this.fluid = fluid;
    }

    public T get() {
        return fluid.get();
    }

    public ResourceLocation getId() {
        return fluid.getId();
    }
}
