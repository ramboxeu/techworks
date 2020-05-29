package io.github.ramboxeu.fabricworks.common.registry;

import io.github.ramboxeu.fabricworks.Techworks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TechworksItems {
    public static BlockItem BOILER;

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, new Identifier(Techworks.MOD_ID, name), item);
    }

    private static <T extends Block> BlockItem register(String name, T block) {
        return register(name, new BlockItem(block, new Item.Settings().group(ItemGroup.REDSTONE)));
    }

    // Full static (no method calls) would be cool, but it's seems like it's getting optimized away
    public static void registerAll() {
        BOILER = register("boiler", TechworksBlocks.BOILER);
    }
}
