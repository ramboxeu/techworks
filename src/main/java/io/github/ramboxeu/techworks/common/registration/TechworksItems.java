package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.item.*;
import io.github.ramboxeu.techworks.common.item.BlueprintItem;
import io.github.ramboxeu.techworks.common.item.PowerAxeItem;
import io.github.ramboxeu.techworks.common.item.SmeltingComponentItem;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import io.github.ramboxeu.techworks.common.registry.ItemDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TechworksItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister();

    public static final List<ItemRegistryObject<BlueprintItem>> BLUEPRINTS = new ArrayList<>();

    public static final ItemRegistryObject<WrenchItem> WRENCH = ITEMS.register("wrench", WrenchItem::new);

    public static final ItemRegistryObject<Item> EMPTY_BLUEPRINT = ITEMS.register("empty_blueprint", Item::new);
    public static final ItemRegistryObject<BlueprintItem> BOILER_BLUEPRINT = registerBlueprint("boiler_blueprint", TechworksBlocks.BOILER, BlueprintItem::new);
    public static final ItemRegistryObject<BlueprintItem> STEAM_ENGINE_BLUEPRINT = registerBlueprint("steam_engine_blueprint", TechworksBlocks.STEAM_ENGINE, BlueprintItem::new);
    public static final ItemRegistryObject<BlueprintItem> ELECTRIC_FURNACE_BLUEPRINT = registerBlueprint("electric_furnace_blueprint", TechworksBlocks.ELECTRIC_FURNACE, BlueprintItem::new);
    public static final ItemRegistryObject<BlueprintItem> ELECTRIC_GRINDER_BLUEPRINT = registerBlueprint("electric_grinder_blueprint", TechworksBlocks.ELECTRIC_GRINDER, BlueprintItem::new);

    public static final ItemRegistryObject<SmeltingComponentItem> ELECTRIFIED_FURNACE = ITEMS.register("electrified_furnace", SmeltingComponentItem::new);
    public static final ItemRegistryObject<SmeltingComponentItem> REINFORCED_ELECTRIFIED_FURNACE = ITEMS.register("reinforced_electrified_furnace", SmeltingComponentItem::new);

    public static final ItemRegistryObject<Item> SMALL_BATTERY = ITEMS.register("small_battery", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> MEDIUM_BATTERY = ITEMS.register("medium_battery", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> LARGE_BATTERY = ITEMS.register("large_battery", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> HICAP_BATTERY = ITEMS.register("hicap_battery", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> SMALL_LIQUID_TANK = ITEMS.register("small_liquid_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> SMALL_GAS_TANK = ITEMS.register("small_gas_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> ROCK_CRUSHER = ITEMS.register("rock_crusher", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> ORE_CRUSHER = ITEMS.register("ore_crusher", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", Item::new);
    public static final ItemRegistryObject<Item> LITHIUM_INGOT = ITEMS.register("lithium_ingot", Item::new);
    public static final ItemRegistryObject<Item> COPPER_DUST = ITEMS.register("copper_dust", Item::new);
    public static final ItemRegistryObject<Item> LITHIUM_DUST = ITEMS.register("lithium_dust", Item::new);
    public static final ItemRegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", Item::new);
    public static final ItemRegistryObject<Item> GOLD_DUST = ITEMS.register("gold_dust", Item::new);
    public static final ItemRegistryObject<Item> CRUSHED_COPPER_ORE = ITEMS.register("crushed_copper_ore", Item::new);
    public static final ItemRegistryObject<Item> CRUSHED_LITHIUM_ORE = ITEMS.register("crushed_lithium_ore", Item::new);
    public static final ItemRegistryObject<Item> CRUSHED_IRON_ORE = ITEMS.register("crushed_iron_ore", Item::new);
    public static final ItemRegistryObject<Item> CRUSHED_GOLD_ORE = ITEMS.register("crushed_gold_ore", Item::new);
    public static final ItemRegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate", Item::new);
    public static final ItemRegistryObject<Item> IRON_GEAR = ITEMS.register("iron_gear", Item::new);
    public static final ItemRegistryObject<Item> STEAM_TURBINE_MK1 = ITEMS.register("steam_turbine_mk1", Item::new);
    public static final ItemRegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", Item::new);
    public static final ItemRegistryObject<PickaxeItem> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", props -> new PickaxeItem(TechworksItemTier.STEEL, 1, -2.8f, props));
    public static final ItemRegistryObject<AxeItem> STEEL_AXE = ITEMS.register("steel_axe", props -> new AxeItem(TechworksItemTier.STEEL, 6.0f, -3.1f, props));
    public static final ItemRegistryObject<ShovelItem> STEEL_SHOVEL = ITEMS.register("steel_shovel", props -> new ShovelItem(TechworksItemTier.STEEL, 1.5f, -3.0f, props));
    public static final ItemRegistryObject<HoeItem> STEEL_HOE = ITEMS.register("steel_hoe", props -> new HoeItem(TechworksItemTier.STEEL, -2, -1.0f, props));
    public static final ItemRegistryObject<SwordItem> STEEL_SWORD = ITEMS.register("steel_sword", props -> new SwordItem(TechworksItemTier.STEEL, 3, -2.4f, props));
    public static final ItemRegistryObject<ArmorItem> STEEL_HELMET = ITEMS.register("steel_helmet", props -> new ArmorItem(TechworksArmorMaterial.STEEL, EquipmentSlotType.HEAD, props));
    public static final ItemRegistryObject<ArmorItem> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate", props -> new ArmorItem(TechworksArmorMaterial.STEEL, EquipmentSlotType.CHEST, props));
    public static final ItemRegistryObject<ArmorItem> STEEL_LEGGINGS = ITEMS.register("steel_leggings", props -> new ArmorItem(TechworksArmorMaterial.STEEL, EquipmentSlotType.LEGS, props));
    public static final ItemRegistryObject<ArmorItem> STEEL_BOOTS = ITEMS.register("steel_boots", props -> new ArmorItem(TechworksArmorMaterial.STEEL, EquipmentSlotType.FEET, props));
    public static final ItemRegistryObject<Item> MEDIUM_LIQUID_TANK = ITEMS.register("medium_liquid_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> LARGE_LIQUID_TANK = ITEMS.register("large_liquid_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> HICAP_LIQUID_TANK = ITEMS.register("hicap_liquid_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> MEDIUM_GAS_TANK = ITEMS.register("medium_gas_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> LARGE_GAS_TANK = ITEMS.register("large_gas_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> HICAP_GAS_TANK = ITEMS.register("hicap_gas_tank", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<Item> ELECTRIC_DRILL = ITEMS.register("electric_drill", props -> new Item(props.maxStackSize(1)));
    public static final ItemRegistryObject<HammerItem> HAMMER = ITEMS.register("hammer", HammerItem::new);
    public static final ItemRegistryObject<WireCuttersItem> WIRE_CUTTERS = ITEMS.register("wire_cutters", WireCuttersItem::new);
    public static final ItemRegistryObject<Item> COPPER_WIRE = ITEMS.register("copper_wire", Item::new);
    public static final ItemRegistryObject<Item> COPPER_PLATE = ITEMS.register("copper_plate", Item::new);
    public static final ItemRegistryObject<Item> IRON_DOUBLE_PLATE = ITEMS.register("iron_double_plate", Item::new);
    public static final ItemRegistryObject<Item> COAL_DUST = ITEMS.register("coal_dust", Item::new);
    public static final ItemRegistryObject<Item> LITHIUM_PLATE = ITEMS.register("lithium_plate", Item::new);

    public static final ItemRegistryObject<Item> MACHINE_CASING = ITEMS.register("machine_casing", Item::new);

    public static final ItemRegistryObject<DebuggerItem> DEBUGGER_ITEM = ITEMS.register("debugger", DebuggerItem::new);

    private static ItemRegistryObject<BlueprintItem> registerBlueprint(String name, BlockRegistryObject<?, ?> machine, IBlueprintItemFactory factory) {
        ItemRegistryObject<BlueprintItem> object = ITEMS.register(name, props -> factory.create(props, machine.get(), machine.getId()));
        BLUEPRINTS.add(object);
        return object;
    }

    @FunctionalInterface
    private interface IBlueprintItemFactory {
        BlueprintItem create(Item.Properties properties, Block block, ResourceLocation recipeId);
    }
}
