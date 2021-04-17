package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.api.component.base.*;
import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.item.BlueprintItem;
import io.github.ramboxeu.techworks.common.item.GroundItem;
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

    // Components
    public static final List<ItemRegistryObject<?>> COMPONENTS = new ArrayList<>();
    public static final List<ItemRegistryObject<BlueprintItem>> BLUEPRINTS = new ArrayList<>();

    public static final ItemRegistryObject<BaseBoilingComponent> BASIC_BOILING_COMPONENT = ITEMS.register("basic_boiling_component", () -> new BaseBoilingComponent(1, 500, 100, 300, 30));
    public static final ItemRegistryObject<BaseBoilingComponent> ADVANCED_BOILING_COMPONENT = ITEMS.register("advanced_boiling_component", () -> new BaseBoilingComponent(2, 1000, 50, 2000, 200));

    public static final ItemRegistryObject<BaseLiquidStorageComponent> BASIC_LIQUID_STORAGE_COMPONENT = ITEMS.register("basic_liquid_storage_component", () -> new BaseLiquidStorageComponent(1, 5000));
    public static final ItemRegistryObject<BaseLiquidStorageComponent> ADVANCED_LIQUID_STORAGE_COMPONENT = ITEMS.register("advanced_liquid_storage_component", () -> new BaseLiquidStorageComponent(2, 10000));

    public static final ItemRegistryObject<BaseGasStorageComponent> BASIC_GAS_STORAGE_COMPONENT = ITEMS.register("basic_gas_storage_component", () -> new BaseGasStorageComponent(1, 5000));
    public static final ItemRegistryObject<BaseGasStorageComponent> ADVANCED_GAS_STORAGE_COMPONENT = ITEMS.register("advanced_gas_storage_component", () -> new BaseGasStorageComponent(2, 10000));

    public static final ItemRegistryObject<BaseSteamTurbineComponent> BASIC_STEAM_TURBINE_COMPONENT = ITEMS.register("basic_steam_turbine_component", () -> new BaseSteamTurbineComponent(1, 500, 100, 150, 15));

    public static final ItemRegistryObject<BaseEnergyStorageComponent> BASIC_ENERGY_STORAGE_COMPONENT = ITEMS.register("basic_energy_storage_component", () -> new BaseEnergyStorageComponent(1, 5000));

    public static final ItemRegistryObject<WrenchItem> WRENCH = ITEMS.register("wrench", WrenchItem::new);

    public static final ItemRegistryObject<GroundItem> GROUND_IRON = ITEMS.register("ground_iron", GroundItem::new);
    public static final ItemRegistryObject<GroundItem> GROUND_GOLD = ITEMS.register("ground_gold", GroundItem::new);

    public static final ItemRegistryObject<Item> EMPTY_BLUEPRINT = ITEMS.register("empty_blueprint", Item::new);
    public static final ItemRegistryObject<BlueprintItem> BOILER_BLUEPRINT = registerBlueprint("boiler_blueprint", TechworksBlocks.BOILER, BlueprintItem::new);
    public static final ItemRegistryObject<BlueprintItem> STEAM_ENGINE_BLUEPRINT = registerBlueprint("steam_engine_blueprint", TechworksBlocks.STEAM_ENGINE, BlueprintItem::new);
    public static final ItemRegistryObject<BlueprintItem> ELECTRIC_FURNACE_BLUEPRINT = registerBlueprint("electric_furnace_blueprint", TechworksBlocks.ELECTRIC_FURNACE, BlueprintItem::new);
    public static final ItemRegistryObject<BlueprintItem> ELECTRIC_GRINDER_BLUEPRINT = registerBlueprint("electric_grinder_blueprint", TechworksBlocks.ELECTRIC_GRINDER, BlueprintItem::new);

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

    static {
        COMPONENTS.add(BASIC_BOILING_COMPONENT);
        COMPONENTS.add(ADVANCED_BOILING_COMPONENT);
        COMPONENTS.add(BASIC_LIQUID_STORAGE_COMPONENT);
        COMPONENTS.add(ADVANCED_LIQUID_STORAGE_COMPONENT);
        COMPONENTS.add(BASIC_GAS_STORAGE_COMPONENT);
        COMPONENTS.add(ADVANCED_GAS_STORAGE_COMPONENT);
        COMPONENTS.add(BASIC_STEAM_TURBINE_COMPONENT);
        COMPONENTS.add(BASIC_ENERGY_STORAGE_COMPONENT);
    }
}
