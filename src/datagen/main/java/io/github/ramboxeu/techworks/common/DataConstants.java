package io.github.ramboxeu.techworks.common;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import net.minecraft.util.ResourceLocation;

import static io.github.ramboxeu.techworks.common.registration.TechworksBlocks.*;
import static io.github.ramboxeu.techworks.common.registration.TechworksItems.*;

public class DataConstants {
    public static class Textures {
        public static final ResourceLocation MACHINE_SIDE = new ResourceLocation(Techworks.MOD_ID, "block/machine_side");
        public static final ResourceLocation MACHINE_TOP = new ResourceLocation(Techworks.MOD_ID, "block/machine_top");
        public static final ResourceLocation MACHINE_BOTTOM = new ResourceLocation(Techworks.MOD_ID, "block/machine_bottom");

        public static final ResourceLocation ITEM_GENERATED = new ResourceLocation("item/generated");
    }

    public static class Items {
        public static final ItemRegistryObject<?>[] BLUEPRINTS = new ItemRegistryObject[]{
                BOILER_BLUEPRINT, STEAM_ENGINE_BLUEPRINT, ELECTRIC_FURNACE_BLUEPRINT, ELECTRIC_GRINDER_BLUEPRINT
        };
    }

    public static class Blocks {
        public static final BlockRegistryObject<?, ?>[] MACHINES = new BlockRegistryObject[]{
                BOILER, STEAM_ENGINE, ELECTRIC_FURNACE, ELECTRIC_GRINDER
        };
    }
}
