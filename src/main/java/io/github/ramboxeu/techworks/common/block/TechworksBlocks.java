package io.github.ramboxeu.techworks.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;

public class TechworksBlocks {
    static final HashSet<Block> BLOCKS = new HashSet<>();
    static final HashSet<Item> ITEM_BLOCKS = new HashSet<>();

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.registerAll((Block[]) BLOCKS.toArray());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll((Item[]) ITEM_BLOCKS.toArray());
    }
}
