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
        public static final ResourceLocation ITEM_HANDHELD = new ResourceLocation("item/handheld");
    }

    public static class Items {
        public static final ItemRegistryObject<?>[] BLUEPRINTS = new ItemRegistryObject[] {
                BOILER_BLUEPRINT, STEAM_ENGINE_BLUEPRINT, ELECTRIC_FURNACE_BLUEPRINT, ELECTRIC_GRINDER_BLUEPRINT
        };

        public static final ItemRegistryObject<?>[] ITEM_GENERATED = new ItemRegistryObject[] {
                ELECTRIFIED_FURNACE, REINFORCED_ELECTRIFIED_FURNACE, SMALL_BATTERY, MEDIUM_BATTERY, LARGE_BATTERY,
                HICAP_BATTERY, SMALL_GAS_TANK, SMALL_LIQUID_TANK, ROCK_CRUSHER, ORE_CRUSHER, COPPER_INGOT, LITHIUM_INGOT,
                COPPER_DUST, LITHIUM_DUST, IRON_DUST, GOLD_DUST, CRUSHED_COPPER_ORE, CRUSHED_LITHIUM_ORE, CRUSHED_IRON_ORE,
                CRUSHED_GOLD_ORE, IRON_GEAR, IRON_PLATE, STEAM_TURBINE_MK1, STEEL_INGOT, STEEL_HELMET, STEEL_CHESTPLATE,
                STEEL_LEGGINGS, STEEL_BOOTS, MEDIUM_LIQUID_TANK, LARGE_LIQUID_TANK, HICAP_LIQUID_TANK, MEDIUM_GAS_TANK,
                LARGE_GAS_TANK, HICAP_GAS_TANK
        };

        public static final ItemRegistryObject<?>[] HANDHELD = new ItemRegistryObject[] {
                STEEL_PICKAXE, STEEL_AXE, STEEL_SHOVEL, STEEL_HOE, STEEL_SWORD
        };
    }

    public static class Blocks {
        public static final BlockRegistryObject<?, ?>[] MACHINES = new BlockRegistryObject[] {
                BOILER, STEAM_ENGINE, ELECTRIC_FURNACE, ELECTRIC_GRINDER, ORE_WASHER, METAL_PRESS, INDUSTRIAL_FURNACE
        };

        public static final BlockRegistryObject<?, ?>[] DROPPING_SELF = new BlockRegistryObject[] {
                BOILER, STEAM_ENGINE, ELECTRIC_FURNACE, ELECTRIC_GRINDER, BLUEPRINT_TABLE, ASSEMBLY_TABLE,
                ENERGY_CABLE, LIQUID_PIPE, GAS_PIPE, ITEM_TRANSPORTER, LITHIUM_ORE, ORE_WASHER
        };

        public static final BlockRegistryObject<?, ?>[] CABLES = new BlockRegistryObject[] {
                ENERGY_CABLE, LIQUID_PIPE, GAS_PIPE, ITEM_TRANSPORTER
        };

        public static final BlockRegistryObject<?, ?>[] DIRECTIONAL_PROCESSING = new BlockRegistryObject[] {
                SOLID_FUEL_BURNER
        };

        public static final BlockRegistryObject<?, ?>[] CUBE_ALL = new BlockRegistryObject[] {
                COPPER_ORE, LITHIUM_ORE
        };
    }

    public static class Misc {
        public static final ResourceLocation CABLE_LOADER = new ResourceLocation(Techworks.MOD_ID, "cable");
    }
}
