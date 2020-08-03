package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class ItemRegistryObject<ITEM extends Item> {
    private final RegistryObject<ITEM> itemRegistryObject;

    public ItemRegistryObject(RegistryObject<ITEM> itemRegistryObject) {
        this.itemRegistryObject = itemRegistryObject;
    }

    public Item getItem() {
        return itemRegistryObject.get();
    }

    public ResourceLocation getRegistryName() {
        return itemRegistryObject.getId();
    }
}
