package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.sync.Event;
import io.github.ramboxeu.techworks.common.component.BasicFluidStorageComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Events {
    public static Event FLUID_STORAGE;

    private static Event register(String name, Event event) {
        return Registry.register(TechworksRegistries.EVENT, new Identifier(Techworks.MOD_ID, name), event);
    }

    public static void registerAll() {
        FLUID_STORAGE = register("fluid_storage", new Event(BasicFluidStorageComponent::serialize, BasicFluidStorageComponent::deserialize));

    }
}
