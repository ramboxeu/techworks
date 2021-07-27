package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public final class ItemRegistryObject<T extends Item> implements IItemSupplier {
    private final RegistryObject<T> item;

    ItemRegistryObject(RegistryObject<T> item) {
        this.item = item;
    }

    public T get() {
        return item.get();
    }

    public ResourceLocation getId() {
        return item.getId();
    }

    @Override
    public Item getAsItem() {
        return item.get();
    }
}
