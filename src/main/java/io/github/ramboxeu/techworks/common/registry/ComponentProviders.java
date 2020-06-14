package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComponentProviders {
    public static IComponentProvider BASIC_BOILING_COMPONENT;

    private static IComponentProvider register(String name, IComponentProvider provider) {
        return Registry.register(TechworksRegistries.COMPONENT_PROVIDER, new Identifier(Techworks.MOD_ID, name), provider);
    }

    public static void registerAll() {
        BASIC_BOILING_COMPONENT = register("basic_boiling_component", TechworksItems.BASIC_BOILING_COMPONENT);
    }
}
