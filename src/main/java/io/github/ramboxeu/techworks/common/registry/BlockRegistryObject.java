package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class BlockRegistryObject<BLOCK extends Block, ITEM extends Item> {
    private final RegistryObject<BLOCK> blockRegistryObject;
    private final RegistryObject<ITEM> itemRegistryObject;

    public BlockRegistryObject(RegistryObject<BLOCK> blockRegistryObject, RegistryObject<ITEM> itemRegistryObject) {
        this.blockRegistryObject = blockRegistryObject;
        this.itemRegistryObject = itemRegistryObject;
    }

    public Block getBlock() {
        return blockRegistryObject.get();
    }

    public Item getItem() {
        return itemRegistryObject.get();
    }

    public ResourceLocation getRegistryName() {
        return blockRegistryObject.getId();
    }
}
