package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ItemDeferredRegister {
    private final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techworks.MOD_ID);

    public <T extends Item> ItemRegistryObject<T> register(String name, Function<Item.Properties, T> itemFactory) {
        return register(name, () -> itemFactory.apply(getDefaultProperties()));
    }

    public <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> itemSupplier) {
        return new ItemRegistryObject<>(ITEMS.register(name, itemSupplier));
    }

    public void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static Item.Properties getDefaultProperties() {
        return new Item.Properties().group(Techworks.ITEM_GROUP);
    }
}
