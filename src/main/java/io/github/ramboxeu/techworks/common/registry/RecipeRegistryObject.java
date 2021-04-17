package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public final class RecipeRegistryObject<T extends IRecipe<IInventory>, U extends IRecipeSerializer<T>> {
    private final ResourceLocation id;
    private final IRecipeType<T> type;
    private final RegistryObject<U> serializer;

    RecipeRegistryObject(IRecipeType<T> type, RegistryObject<U> serializer) {
        this.type = type;
        this.serializer = serializer;

        id = serializer.getId();
    }

    public IRecipeType<T> get() {
        return type;
    }

    public U getSerializer() {
        return serializer.get();
    }

    public ResourceLocation getId() {
        return id;
    }
}
