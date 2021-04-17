package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public final class TileRegistryObject<T extends TileEntity> {
    private final RegistryObject<TileEntityType<T>> tile;

    public TileRegistryObject(RegistryObject<TileEntityType<T>> tile) {
        this.tile = tile;
    }

    public TileEntityType<T> get() {
        return tile.get();
    }

    public ResourceLocation getId() {
        return tile.getId();
    }
}
