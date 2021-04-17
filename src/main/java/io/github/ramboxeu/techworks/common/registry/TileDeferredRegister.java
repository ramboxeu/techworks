package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class TileDeferredRegister {
    private final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Techworks.MOD_ID);

    @SuppressWarnings("ConstantConditions")
    public <T extends TileEntity> TileRegistryObject<T> register(BlockRegistryObject<?, ?> block, Supplier<T> tileSupplier) {
        return register(block.getId().getPath(), () -> TileEntityType.Builder.create(tileSupplier, block.get()).build(null));
    }

    public <T extends TileEntity> TileRegistryObject<T> register(String name, Supplier<TileEntityType<T>> typeSupplier) {
        return new TileRegistryObject<>(TILES.register(name, typeSupplier));
    }

    public void register(IEventBus bus) {
        TILES.register(bus);
    }
}
