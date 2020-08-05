package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockDeferredRegister {
    private final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Techworks.MOD_ID);
    private final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techworks.MOD_ID);
    private final List<BlockRegistryObject<?, ?>> REGISTERED_BLOCKS = new ArrayList<>();

    public <BLOCK extends Block> BlockRegistryObject<BLOCK, BlockItem> register(String name, Supplier<BLOCK> blockSupplier) {
        return register(name, blockSupplier, block -> new BlockItem(block, ItemDeferredRegister.getDefaultProperties()));
    }

    public <BLOCK extends Block, ITEM extends Item> BlockRegistryObject<BLOCK, ITEM> register(String name, Supplier<BLOCK> blockSupplier, Function<BLOCK, ITEM> itemFactory) {
        RegistryObject<BLOCK> block = BLOCKS.register(name, blockSupplier);
        BlockRegistryObject<BLOCK, ITEM> object = new BlockRegistryObject<>(block, ITEMS.register(name, () -> itemFactory.apply(block.get())));
        REGISTERED_BLOCKS.add(object);
        return object;
    }

    public List<BlockRegistryObject<?, ?>> getRegisteredBlocks() {
        return REGISTERED_BLOCKS;
    }

    public void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
