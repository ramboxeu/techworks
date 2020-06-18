package io.github.ramboxeu.techworks.common.registry;


import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.component.BasicBoilingComponent;
import io.github.ramboxeu.techworks.common.component.BasicFluidStorageComponent;
import io.github.ramboxeu.techworks.common.item.ComponentItem;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TechworksItems {
    public static BlockItem BOILER;
    public static ComponentItem<BasicBoilingComponent> BASIC_BOILING_COMPONENT;
    public static ComponentItem<BasicFluidStorageComponent> BASIC_FLUID_STORAGE_COMPONENT;
    public static WrenchItem WRENCH;

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(Techworks.MOD_ID, name), item);
    }

    private static <T extends Block> BlockItem register(String name, T block) {
        return register(name, new BlockItem(block, new Item.Settings().group(ItemGroup.REDSTONE)));
    }

    // Full static (no method calls) would be cool, but it's seems like it's getting optimized away
    public static void registerAll() {
        BOILER = register("boiler", TechworksBlocks.BOILER);
        BASIC_BOILING_COMPONENT = register("basic_boiling_component", new ComponentItem<>(BasicBoilingComponent::new));
        BASIC_FLUID_STORAGE_COMPONENT = register("basic_fluid_storage_component", new ComponentItem<>(BasicFluidStorageComponent::new));
        WRENCH = register("wrench", new WrenchItem());
    }
}
