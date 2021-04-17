package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BlockDeferredRegister {
    private final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Techworks.MOD_ID);
    private final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techworks.MOD_ID);

    public <T extends Block> BlockRegistryObject<T, BlockItem> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, block -> new BlockItem(block, ItemDeferredRegister.getDefaultProperties()));
    }

    public <T extends Block, U extends Item> BlockRegistryObject<T, U> register(String name, Supplier<T> blockSupplier, Function<T, U> itemFactory) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        RegistryObject<U> item = ITEMS.register(name, () -> itemFactory.apply(block.get()));

        return new BlockRegistryObject<>(block, item);
    }

    public void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
