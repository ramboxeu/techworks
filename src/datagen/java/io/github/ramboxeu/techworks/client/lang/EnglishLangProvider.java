package io.github.ramboxeu.techworks.client.lang;

import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import net.minecraft.data.DataGenerator;

public class EnglishLangProvider extends BaseLangProvider {

    public EnglishLangProvider(DataGenerator gen) {
        super(gen, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Blocks
        add(TechworksBlocks.ASSEMBLY_TABLE, "Assembly Table");
        add(TechworksBlocks.BOILER, "Boiler");
        add(TechworksBlocks.STEAM_ENGINE, "Steam Engine");
        add(TechworksBlocks.ELECTRIC_GRINDER, "Electric Grinder");
        add(TechworksBlocks.ELECTRIC_FURNACE, "Electric Furnace");
        add(TechworksBlocks.BLUEPRINT_TABLE, "Blueprint Table");
        add(TechworksBlocks.ITEM_TRANSPORTER, "Item Transporter");
        add(TechworksBlocks.LIQUID_PIPE, "Liquid Pipe");
        add(TechworksBlocks.GAS_PIPE, "Gas Pipe");
        add(TechworksBlocks.ENERGY_CABLE, "Energy Cable");
        add(TechworksBlocks.SOLID_FUEL_BURNER, "Solid Fuel Burner");
        add(TechworksBlocks.COPPER_ORE, "Copper Ore");
        add(TechworksBlocks.LITHIUM_ORE, "Lithium Ore");
        add(TechworksBlocks.ORE_WASHER, "Ore Washer");
        add(TechworksBlocks.METAL_PRESS, "Metal Press");
        add(TechworksBlocks.INDUSTRIAL_FURNACE, "Industrial Furnace");
        add(TechworksBlocks.LIQUID_TANK, "Liquid Tank");
        add(TechworksBlocks.GAS_TANK, "Gas Tank");
        add(TechworksBlocks.ENERGY_STORAGE, "Energy Storage");
        add(TechworksBlocks.LIQUID_PUMP, "Liquid Pump");
        add(TechworksBlocks.ORE_MINER, "Ore Miner");

        // Items
        add(TechworksItems.WRENCH, "Wrench");
        add(TechworksItems.EMPTY_BLUEPRINT, "Empty Blueprint");
        add(TechworksItems.BOILER_BLUEPRINT, "Boiler Blueprint");
        add(TechworksItems.STEAM_ENGINE_BLUEPRINT, "Steam Engine Blueprint");
        add(TechworksItems.ELECTRIC_FURNACE_BLUEPRINT, "Electric Furnace Blueprint");
        add(TechworksItems.ELECTRIC_GRINDER_BLUEPRINT, "Electric Grinder Blueprint");
        add(TechworksItems.ELECTRIFIED_FURNACE, "Electrified Furnace");
        add(TechworksItems.REINFORCED_ELECTRIFIED_FURNACE, "Reinforced Electrified Furnace");
        add(TechworksItems.SMALL_BATTERY, "Small Battery");
        add(TechworksItems.MEDIUM_BATTERY, "Medium Battery");
        add(TechworksItems.LARGE_BATTERY, "Large Battery");
        add(TechworksItems.HICAP_BATTERY, "HiCap\u2122 Battery");
        add(TechworksItems.SMALL_LIQUID_TANK, "Small Liquid Tank");
        add(TechworksItems.SMALL_GAS_TANK, "Small Gas Tank");
        add(TechworksItems.ROCK_CRUSHER, "RockCrusher");
        add(TechworksItems.ORE_CRUSHER, "OreCrusher");
        add(TechworksItems.COPPER_INGOT, "Copper Ingot");
        add(TechworksItems.LITHIUM_INGOT, "Lithium Ingot");
        add(TechworksItems.COPPER_DUST, "Copper Dust");
        add(TechworksItems.LITHIUM_DUST, "Lithium Dust");
        add(TechworksItems.IRON_DUST, "Iron Dust");
        add(TechworksItems.GOLD_DUST, "Gold Dust");
        add(TechworksItems.CRUSHED_COPPER_ORE, "Crushed Copper Ore");
        add(TechworksItems.CRUSHED_LITHIUM_ORE, "Crushed Lithium Ore");
        add(TechworksItems.CRUSHED_IRON_ORE, "Crushed Iron Ore");
        add(TechworksItems.CRUSHED_GOLD_ORE, "Crushed Gold Ore");
        add(TechworksItems.IRON_PLATE, "Iron Plate");
        add(TechworksItems.IRON_GEAR, "Iron Gear");
        add(TechworksItems.STEAM_TURBINE_MK1, "Steam Turbine Mk1");
        add(TechworksItems.STEEL_INGOT, "Steel Ingot");
        add(TechworksItems.STEEL_PICKAXE, "Steel Pickaxe");
        add(TechworksItems.STEEL_AXE, "Steel Axe");
        add(TechworksItems.STEEL_SHOVEL, "Steel Shovel");
        add(TechworksItems.STEEL_HOE, "Steel Hoe");
        add(TechworksItems.STEEL_SWORD, "Steel Sword");
        add(TechworksItems.STEEL_HELMET, "Steel Helmet");
        add(TechworksItems.STEEL_CHESTPLATE, "Steel Chestplate");
        add(TechworksItems.STEEL_LEGGINGS, "Steel Leggings");
        add(TechworksItems.STEEL_BOOTS, "Steel Boots");
        add(TechworksItems.MEDIUM_LIQUID_TANK, "Medium Liquid Tank");
        add(TechworksItems.LARGE_LIQUID_TANK, "Large Liquid Tank");
        add(TechworksItems.HICAP_LIQUID_TANK, "HiCap\u2122 Liquid Tank");
        add(TechworksItems.MEDIUM_GAS_TANK, "Medium Gas Tank");
        add(TechworksItems.LARGE_GAS_TANK, "Large Gas Tank");
        add(TechworksItems.HICAP_GAS_TANK, "HiCap\u2122 Gas Tank");
        add(TechworksItems.ELECTRIC_DRILL, "Electric Drill");
        add(TechworksItems.HAMMER, "Hammer");
        add(TechworksItems.WIRE_CUTTERS, "Wire Cutters");
        add(TechworksItems.COPPER_WIRE, "Copper Wire");
        add(TechworksItems.COPPER_PLATE, "Copper Plate");
        add(TechworksItems.IRON_DOUBLE_PLATE, "Iron Double Plate");
        add(TechworksItems.COAL_DUST, "Coal Dust");

        // Containers
        add(TranslationKeys.ASSEMBLY_TABLE, "Assembly Table");
        add(TranslationKeys.BLUEPRINT_TABLE, "Blueprint Table");
        add(TranslationKeys.BOILER, "Boiler");
        add(TranslationKeys.ELECTRIC_FURNACE, "Electric Furnace");
        add(TranslationKeys.ELECTRIC_GRINDER, "Electric Grinder");
        add(TranslationKeys.SOLID_FUEL_BURNER, "Solid Fuel Burner");
        add(TranslationKeys.STEAM_ENGINE, "Steam Engine");
        add(TranslationKeys.ORE_WASHER, "Ore Washer");
        add(TranslationKeys.METAL_PRESS, "Metal Press");
        add(TranslationKeys.INDUSTRIAL_FURNACE, "Industrial Furnace");
        add(TranslationKeys.LIQUID_PUMP, "Liquid Pump");
        add(TranslationKeys.ORE_MINER, "Ore Miner");

        // Tooltips
        add(TranslationKeys.ENERGY_STORAGE_CAPACITY, "Capacity: %dFE");
        add(TranslationKeys.FLUID_STORAGE_CAPACITY, "Capacity: %dmB");
        add(TranslationKeys.ENERGY_TRANSFER_RATE, "Transfer Rate: %dFE/t");
        add(TranslationKeys.FLUID_TRANSFER_RATE, "Transfer Rate: %dmB/t");
        add(TranslationKeys.ENERGY_CAP, "Energy Cap: %dFE/t");
        add(TranslationKeys.BONUS_ENERGY_CAP, "Energy Cap: %dFE/t (+%d Efficiency Bonus)");
        add(TranslationKeys.ENERGY_MODIFIER, "Energy Modifier: %dx");
        add(TranslationKeys.ORE_CRUSHER_NOTE, "Can only be used to crush ores");
        add(TranslationKeys.STORED_FLUID, "%s %d/%dmB");
        add(TranslationKeys.STORED_ENERGY, "%d/%dFE");
        add(TranslationKeys.HEAT_DISPLAY, "%dHU");
        add(TranslationKeys.GEAR, "Gear");
        add(TranslationKeys.PLATE, "Plate");
        add(TranslationKeys.TEMPERATURE, "Temperature: %dHU");
        add(TranslationKeys.LIQUID_STORED, "Liquid Stored: ");
        add(TranslationKeys.FLUID_AMOUNT_STORED, "Amount Stored: %dmB");
        add(TranslationKeys.GAS_STORED, "Gas Stored: ");
        add(TranslationKeys.ENERGY_STORED, "Energy Stored: %dFE");
        add(TranslationKeys.CONFIGURED, "Configured");
        add(TranslationKeys.GO_BACK, "Go Back");
        add(TranslationKeys.RIGHT_CLICK_TO_CLEAR, "Right click to clear");
        add(TranslationKeys.PUT_STACK_TO_SET_FILTER, "Left click with an ItemStack to set");
        add(TranslationKeys.LEFT_CLICK_TO_CONFIGURE, "Left click to configure");
        add(TranslationKeys.SET_ORE_FILTER_STACK, "Set the ResourceFilter to an ItemStack");
        add(TranslationKeys.SET_ORE_FILTER_TAG, "Set the ResourceFilter to a Tag");
        add(TranslationKeys.CLEAR_ORE_FILTER, "Clear the ResourceFilter");
        add(TranslationKeys.ENERGY_USAGE, "Energy Usage: %dFE/t");
        add(TranslationKeys.NO_MATCHING_BLOCKS_FOUND, "Previous scan didn't found any matching blocks");
        add(TranslationKeys.WAITING_TICKS, "Waiting %d ticks");
        add(TranslationKeys.SCAN_NOW, "Click to scan now");
        add(TranslationKeys.MINING_TIME, "Mining time: %d ticks");
        add(TranslationKeys.WIRE, "Wire");
        add(TranslationKeys.DOUBLE_PLATE, "Double Plate");

        // Widgets
        add(TranslationKeys.COMPONENTS, "Components");
        add(TranslationKeys.IDLE, "Idle");
        add(TranslationKeys.INSTALLING, "Installing");
        add(TranslationKeys.UNINSTALLING, "Uninstalling");
        add(TranslationKeys.IO, "I/O");
        add(TranslationKeys.NONE_INPUT_TYPE, "None");
        add(TranslationKeys.ITEM_INPUT_TYPE, "Item");
        add(TranslationKeys.LIQUID_INPUT_TYPE, "Liquid");
        add(TranslationKeys.GAS_INPUT_TYPE, "Gas");
        add(TranslationKeys.ENERGY_INPUT_TYPE, "Energy");
        add(TranslationKeys.BOTTOM_SIDE, "Bottom");
        add(TranslationKeys.TOP_SIDE, "Top");
        add(TranslationKeys.FRONT_SIDE, "Front");
        add(TranslationKeys.LEFT_SIDE, "Left");
        add(TranslationKeys.RIGHT_SIDE, "Right");
        add(TranslationKeys.BACK_SIDE, "Back");
        add(TranslationKeys.INPUT, "Input");
        add(TranslationKeys.OUTPUT, "Output");
        add(TranslationKeys.BOTH, "Both");
        add(TranslationKeys.MODE, "Mode");
        add(TranslationKeys.ENABLED, "Enabled");
        add(TranslationKeys.INFO, "Info");
        add(TranslationKeys.REDSTONE, "Redstone");
        add(TranslationKeys.IGNORED, "Ignored");
        add(TranslationKeys.HIGH, "High");
        add(TranslationKeys.LOW, "Low");
        add(TranslationKeys.STATUS, "Status");
        add(TranslationKeys.ACTIVE, "Active");
        add(TranslationKeys.STANDBY, "Standby");

        // Fluids
        add(TechworksFluids.STEAM, "Steam");
        add(TranslationKeys.EMPTY, "Empty");

        // Components
        add(TranslationKeys.ELECTRIFIED_FURNACE, "Electrified Furnace");
        add(TranslationKeys.REINFORCED_ELECTRIFIED_FURNACE, "Reinforced Electrified Furnace");
        add(TranslationKeys.SOLID_FUEL_BURNER_COMPONENT, "Solid Fuel Burner");
        add(TranslationKeys.ROCK_CRUSHER, "RockCrusher");
        add(TranslationKeys.ORE_CRUSHER, "OreCrusher");
        add(TranslationKeys.HICAP_BATTERY, "HiCap\u2122 Battery");
        add(TranslationKeys.LARGE_BATTERY, "Large Battery");
        add(TranslationKeys.MEDIUM_BATTERY, "Medium Battery");
        add(TranslationKeys.SMALL_BATTERY, "Small Battery");
        add(TranslationKeys.SMALL_GAS_TANK, "Small Gas Tank");
        add(TranslationKeys.SMALL_LIQUID_TANK, "Small Liquid Tank");
        add(TranslationKeys.STEAM_TURBINE_MK1, "Steam Turbine Mk1");
        add(TranslationKeys.ELECTRIC_DRILL, "Electric Drill");
        add(TechworksComponents.ENERGY_STORAGE, "Energy Storage Component");
        add(TechworksComponents.GAS_STORAGE, "Gas Storage Component");
        add(TechworksComponents.LIQUID_STORAGE, "Liquid Storage Component");
        add(TechworksComponents.GRINDING, "Grinding Component");
        add(TechworksComponents.HEATING, "Heating Component");
        add(TechworksComponents.SMELTING, "Smelting Component");
        add(TechworksComponents.STEAM_TURBINE, "Steam Turbine");
        add(TechworksComponents.MINING, "Mining Component");

        // Statues
        add(TranslationKeys.ENGINE_ALREADY_LINKED, "Engine (%d %d %d) is already linked to boiler (%d %d %d)");
        add(TranslationKeys.ENGINE_LINKING_SUCCESS, "Linked engine (%d %d %d) to boiler (%d %d %d)");
        add(TranslationKeys.ENGINE_LINKING_FAILURE, "Linking engine (%d %d %d) to %d %d %d failed");
        add(TranslationKeys.ENGINE_LINKING_CANCELLED, "Linking to %d %d %d was canceled");
        add(TranslationKeys.LINKING_ENGINES, "Linking to %d %d %d");
        add(TranslationKeys.ENGINE_UNLINKED, "Unliked engine (%d %d %d) from boiler (%d %d %d)");
        add(TranslationKeys.ENGINE_UNLINKING_FAILURE, "Unlinking engine (%d %d %d) from boiler (%d %d %d) failed");
        add(TranslationKeys.COMPONENT_ALREADY_INSTALLED, "Component is already installed");
        add(TranslationKeys.COMPONENT_CAPACITY_TOO_SMALL, "Component's capacity is too small and would result in overflow");
        add(TranslationKeys.COMPONENT_INSTALLED, "Component installed");
        add(TranslationKeys.CANT_INSTALL_COMPONENT, "Unable to install: ");
        add(TranslationKeys.CANT_UNINSTALL_COMPONENT, "Unable to uninstall: ");
        add(TranslationKeys.BASE_COMPONENT_INSTALLED, "Base component is installed");
        add(TranslationKeys.INSTALLED_COMPONENT, "Currently installed component: ");
        add(TranslationKeys.COMPONENT_UNINSTALLED, "Component uninstalled");

        // Generic
        add(TranslationKeys.STATUS_IDLE, "Idle");
        add(TranslationKeys.STATUS_MINING, "Mining");
        add(TranslationKeys.STATUS_SCANNING, "Scanning");
        add(TranslationKeys.RESOURCE, "Resource Filter");
        add(TranslationKeys.BLOCKS_TO_MINE, "Blocks to Mine");
        add(TranslationKeys.BLOCKS_MINED, "Blocks Mined");
        add(TranslationKeys.AREA, "Area");
        add(TranslationKeys.CHUNKS, "chunks");
        add(TranslationKeys.NOT_SET, "Not Set");
        add(TranslationKeys.BLOCK, "Block");
        add(TranslationKeys.CONFIGURE, "Configure");
        add(TranslationKeys.ITEM_STACK, "ItemStack");
        add(TranslationKeys.TAG, "Tag");
        add(TranslationKeys.CLEAR, "Clear");
        add(TranslationKeys.CONFIRM, "Confirm");
        add(TranslationKeys.INVALID_TAG, "Invalid Tag name");
        add(TranslationKeys.ENTER_TAG_NAME, "Enter Tag name");

        // Other
        add("itemGroup.techworks", "Techworks");
    }
}
