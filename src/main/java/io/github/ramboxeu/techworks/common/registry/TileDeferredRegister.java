package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TileDeferredRegister {
    private final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Techworks.MOD_ID);

    @SuppressWarnings("ConstantConditions")
    public <TILE extends TileEntity> TileRegistryObject<TILE> register(BlockRegistryObject<?, ?> block, Supplier<TILE> tileSupplier) {
        return register(block.getRegistryName().getPath(), () -> TileEntityType.Builder.create(tileSupplier, block.getBlock()).build(null));
    }

    public <TILE extends TileEntity> TileRegistryObject<TILE> register(String name, Supplier<TileEntityType<TILE>> typeSupplier) {
        Techworks.LOGGER.debug("Registering: {} tile", name);
        return new TileRegistryObject<>(TILES.register(name, typeSupplier));
    }

    public void register(IEventBus bus) {
        TILES.register(bus);
    }
}
