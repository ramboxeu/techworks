package io.github.ramboxeu.techworks.common.registration;

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

    public static final List<ItemRegistryObject<BlueprintItem>> BLUEPRINTS = new ArrayList<>();

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
}
