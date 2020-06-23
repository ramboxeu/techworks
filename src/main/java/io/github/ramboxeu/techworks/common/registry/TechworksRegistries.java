package io.github.ramboxeu.techworks.common.registry;

import com.mojang.serialization.Lifecycle;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import io.github.ramboxeu.techworks.common.api.sync.Event;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class TechworksRegistries {
    public static final RegistryKey<Registry<ComponentType>> COMPONENT_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(Techworks.MOD_ID, "component_type"));
    public static final RegistryKey<Registry<IComponentProvider>> COMPONENT_PROVIDER_KEY = RegistryKey.ofRegistry(new Identifier(Techworks.MOD_ID, "component_key"));
    public static final RegistryKey<Registry<Event>> EVENT_KEY = RegistryKey.ofRegistry(new Identifier(Techworks.MOD_ID, "event"));

    public static Registry<ComponentType> COMPONENT_TYPE = register("component_type",
            new SimpleRegistry<>(COMPONENT_TYPE_KEY, Lifecycle.experimental()), COMPONENT_TYPE_KEY);

    public static Registry<IComponentProvider> COMPONENT_PROVIDER = register("component_provider",
            new SimpleRegistry<>(COMPONENT_PROVIDER_KEY, Lifecycle.experimental()), COMPONENT_PROVIDER_KEY);

    public static Registry<Event> EVENT = register("event",
            new SimpleRegistry<>(EVENT_KEY, Lifecycle.experimental()), EVENT_KEY);

    // It will be improved in the near feature
    private static <T, U extends Registry<T>> U register(String name, U registry, RegistryKey<U> registryKey) {
        //noinspection unchecked,rawtypes
        return (U) ((MutableRegistry)Registry.REGISTRIES).add(registryKey, registry); // new Identifier(Techworks.MOD_ID, name), registry);
    }
}
