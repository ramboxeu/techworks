package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class RecipeDeferredRegister {
    private final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Techworks.MOD_ID);

    public <T extends IRecipe<IInventory>, U extends IRecipeSerializer<T>> RecipeRegistryObject<T, U> register(String name, Supplier<U> supplier) {
        RegistryObject<U> serializer = SERIALIZERS.register(name, supplier);
        IRecipeType<T> type = IRecipeType.register(serializer.getId().toString());

        return new RecipeRegistryObject<>(type, serializer);
    }

    public void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
