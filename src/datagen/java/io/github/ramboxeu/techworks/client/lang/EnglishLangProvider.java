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
        add(TechworksBlocks.METAL_PRESS, "Metal Press");

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
        add(TechworksItems.HICAP_BATTERY, "HiCap™ Battery");
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
        add(TranslationKeys.HICAP_BATTERY, "HiCap™ Battery");
        add(TranslationKeys.LARGE_BATTERY, "Large Battery");
        add(TranslationKeys.MEDIUM_BATTERY, "Medium Battery");
        add(TranslationKeys.SMALL_BATTERY, "Small Battery");
        add(TranslationKeys.SMALL_GAS_TANK, "Small Gas Tank");
        add(TranslationKeys.SMALL_LIQUID_TANK, "Small Liquid Tank");
        add(TechworksComponents.ENERGY_STORAGE, "Energy Storage Component");
        add(TechworksComponents.GAS_STORAGE, "Gas Storage Component");
        add(TechworksComponents.LIQUID_STORAGE, "Liquid Storage Component");
        add(TechworksComponents.GRINDING, "Grinding Component");
        add(TechworksComponents.HEATING, "Heating Component");
        add(TechworksComponents.SMELTING, "Smelting Component");

        // Other
        add("itemGroup.techworks", "Techworks");
    }
}
