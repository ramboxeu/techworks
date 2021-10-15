package io.github.ramboxeu.techworks.common.lang;

import io.github.ramboxeu.techworks.common.registration.TechworksContainers;

public class TranslationKeys {
    // Containers
    public static final TranslationKey BOILER = TranslationKey.container(TechworksContainers.BOILER);
    public static final TranslationKey STEAM_ENGINE = TranslationKey.container(TechworksContainers.STEAM_ENGINE);
    public static final TranslationKey ELECTRIC_GRINDER = TranslationKey.container(TechworksContainers.ELECTRIC_GRINDER);
    public static final TranslationKey ELECTRIC_FURNACE = TranslationKey.container(TechworksContainers.ELECTRIC_FURNACE);
    public static final TranslationKey BLUEPRINT_TABLE = TranslationKey.container(TechworksContainers.BLUEPRINT_TABLE);
    public static final TranslationKey ASSEMBLY_TABLE = TranslationKey.container(TechworksContainers.ASSEMBLY_TABLE);
    public static final TranslationKey SOLID_FUEL_BURNER = TranslationKey.container(TechworksContainers.SOLID_FUEL_BURNER);
    public static final TranslationKey ORE_WASHER = TranslationKey.container(TechworksContainers.ORE_WASHER);
    public static final TranslationKey METAL_PRESS = TranslationKey.container(TechworksContainers.METAL_PRESS);

    // Tooltips
    public static final TranslationKey ENERGY_STORAGE_CAPACITY = TranslationKey.tooltip("energy_storage_capacity");
    public static final TranslationKey FLUID_STORAGE_CAPACITY = TranslationKey.tooltip("fluid_storage_capacity");
    public static final TranslationKey ENERGY_TRANSFER_RATE = TranslationKey.tooltip("energy_transfer_rate");
    public static final TranslationKey FLUID_TRANSFER_RATE = TranslationKey.tooltip("fluid_transfer_rate");
    public static final TranslationKey ENERGY_CAP = TranslationKey.tooltip("energy_cap");
    public static final TranslationKey BONUS_ENERGY_CAP = TranslationKey.tooltip("bonus_energy_cap");
    public static final TranslationKey ENERGY_MODIFIER = TranslationKey.tooltip("energy_modifier");
    public static final TranslationKey ORE_CRUSHER_NOTE = TranslationKey.tooltip("ore_crusher_note");
    public static final TranslationKey STORED_FLUID = TranslationKey.tooltip("stored_fluid");
    public static final TranslationKey STORED_ENERGY = TranslationKey.tooltip("stored_energy");
    public static final TranslationKey HEAT_DISPLAY = TranslationKey.tooltip("heat_display");
    public static final TranslationKey GEAR = TranslationKey.tooltip("gear");
    public static final TranslationKey PLATE = TranslationKey.tooltip("plate");

    // Widgets
    public static final TranslationKey COMPONENTS = TranslationKey.widget("components");
    public static final TranslationKey IDLE = TranslationKey.widget("components", "idle");
    public static final TranslationKey INSTALLING = TranslationKey.widget("components", "installing");
    public static final TranslationKey UNINSTALLING = TranslationKey.widget("components", "uninstalling");
    public static final TranslationKey IO = TranslationKey.widget("io");
    public static final TranslationKey NONE_INPUT_TYPE = TranslationKey.widget("io", "none_input_type");
    public static final TranslationKey ITEM_INPUT_TYPE = TranslationKey.widget("io", "item_input_type");
    public static final TranslationKey LIQUID_INPUT_TYPE = TranslationKey.widget("io", "liquid_input_type");
    public static final TranslationKey GAS_INPUT_TYPE = TranslationKey.widget("io", "gas_input_type");
    public static final TranslationKey ENERGY_INPUT_TYPE = TranslationKey.widget("io", "energy_input_type");
    public static final TranslationKey BOTTOM_SIDE = TranslationKey.widget("io", "bottom_side");
    public static final TranslationKey TOP_SIDE = TranslationKey.widget("io", "top_side");
    public static final TranslationKey FRONT_SIDE = TranslationKey.widget("io", "front_side");
    public static final TranslationKey LEFT_SIDE = TranslationKey.widget("io", "left_side");
    public static final TranslationKey RIGHT_SIDE = TranslationKey.widget("io", "right_side");
    public static final TranslationKey BACK_SIDE = TranslationKey.widget("io", "back_side");
    public static final TranslationKey INPUT = TranslationKey.widget("io", "input");
    public static final TranslationKey OUTPUT = TranslationKey.widget("io", "output");
    public static final TranslationKey BOTH = TranslationKey.widget("io", "both");
    public static final TranslationKey MODE = TranslationKey.widget("io", "mode");
    public static final TranslationKey ENABLED = TranslationKey.widget("io", "enabled");
    public static final TranslationKey INFO = TranslationKey.widget("io", "info");

    // Fluids
    public static final TranslationKey EMPTY = TranslationKey.fluid("empty");

    // Components
    public static final TranslationKey ELECTRIFIED_FURNACE = TranslationKey.component("electrified_furnace");
    public static final TranslationKey REINFORCED_ELECTRIFIED_FURNACE = TranslationKey.component("reinforced_electrified_furnace");
    public static final TranslationKey SOLID_FUEL_BURNER_COMPONENT = TranslationKey.component("solid_fuel_burner");
    public static final TranslationKey ROCK_CRUSHER = TranslationKey.component("rock_crusher");
    public static final TranslationKey ORE_CRUSHER = TranslationKey.component("ore_crusher");
    public static final TranslationKey HICAP_BATTERY = TranslationKey.component("hicap_battery");
    public static final TranslationKey LARGE_BATTERY = TranslationKey.component("large_battery");
    public static final TranslationKey MEDIUM_BATTERY = TranslationKey.component("medium_battery");
    public static final TranslationKey SMALL_BATTERY = TranslationKey.component("small_battery");
    public static final TranslationKey SMALL_GAS_TANK = TranslationKey.component("small_gas_tank");
    public static final TranslationKey SMALL_LIQUID_TANK = TranslationKey.component("small_liquid_tank");
}
