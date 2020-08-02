package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class TileRegistryObject<TILE extends TileEntity> {
    private final RegistryObject<TileEntityType<TILE>> tileTypeRegistryObject;

    public TileRegistryObject(RegistryObject<TileEntityType<TILE>> tileTypeRegistryObject) {
        this.tileTypeRegistryObject = tileTypeRegistryObject;
    }

    public TileEntityType<TILE> getTileType() {
        return tileTypeRegistryObject.get();
    }

    public ResourceLocation getRegistryName() {
        return tileTypeRegistryObject.getId();
    }
}
