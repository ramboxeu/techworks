package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.component.BoilingComponentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComponentTypes {
    public static ComponentType BOILING_COMPONENT;

    private static ComponentType register(String name, ComponentType type) {
        return Registry.register(TechworksRegistries.COMPONENT_TYPE, new Identifier(Techworks.MOD_ID, name), type);
    }

    public static void registerAll() {
        BOILING_COMPONENT = register("boiling_component", new BoilingComponentType());
    }
}
