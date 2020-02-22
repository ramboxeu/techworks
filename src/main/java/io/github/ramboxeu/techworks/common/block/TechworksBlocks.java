package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Techworks.MOD_ID)
public class TechworksBlocks {
    private static final Block[] blocks = {
            new BoilerBlock()
    };

    @ObjectHolder("boiler")
    public static final Block boiler = null;

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for (Block block : blocks) {
            registry.register(block);
        }
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (Block block : blocks) {
            registry.register(new BlockItem(block, new Item.Properties().group(Techworks.ITEM_GROUP)).setRegistryName(block.getRegistryName()));
        }
    }
}
