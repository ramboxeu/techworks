package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public final class BlockRegistryObject<T extends Block, U extends Item> {
    private final RegistryObject<T> block;
    private final RegistryObject<U> item;

    BlockRegistryObject(RegistryObject<T> block, RegistryObject<U> item) {
        this.block = block;
        this.item = item;
    }

    public T get() {
        return block.get();
    }

    public U getItem() {
        return item.get();
    }

    public ResourceLocation getId() {
        return block.getId();
    }
}
