package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class TechworksRegistries {

    public static IForgeRegistry<ComponentType<?>> COMPONENT_TYPES;

    public static void register() {
        COMPONENT_TYPES = new RegistryBuilder<ComponentType<?>>()
                .setType(Utils.cast(ComponentType.class))
                .setName(new ResourceLocation(Techworks.MOD_ID, "component_types"))
                .create();
    }
}
