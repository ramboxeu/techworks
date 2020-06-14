package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class TechworksRegistries {
    public static SimpleRegistry<ComponentType> COMPONENT_TYPE = register("component_type", new SimpleRegistry<>());
    public static SimpleRegistry<IComponentProvider> COMPONENT_PROVIDER = register("component_provider", new SimpleRegistry<>());

    private static <T> SimpleRegistry<T> register(String name, SimpleRegistry<T> registry) {
        return Registry.register(Registry.REGISTRIES, new Identifier(Techworks.MOD_ID, name), registry);
    }
}
