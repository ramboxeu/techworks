package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.item.BlueprintItem;
import io.github.ramboxeu.techworks.common.item.SmeltingComponentItem;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import io.github.ramboxeu.techworks.common.registry.ItemDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
