package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class MetalPressingRecipeBuilder {
    private final Ingredient ingredient;
    private final IRecipeResult result;
    private final MetalPressTile.Mode mode;

    public MetalPressingRecipeBuilder(Ingredient ingredient, IRecipeResult result, MetalPressTile.Mode mode) {
        this.ingredient = ingredient;
        this.result = result;
        this.mode = mode;
    }

    public static MetalPressingRecipeBuilder plate(Ingredient ingredient, IRecipeResult result) {
        return new MetalPressingRecipeBuilder(ingredient, result, MetalPressTile.Mode.PLATE);
    }

    public static MetalPressingRecipeBuilder gear(Ingredient ingredient, IRecipeResult result) {
        return new MetalPressingRecipeBuilder(ingredient, result, MetalPressTile.Mode.GEAR);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Result(TechworksRecipes.METAL_PRESSING.getSerializer(), ingredient, result, new ResourceLocation(Techworks.MOD_ID, name), mode));
    }

    public static class Result extends BaseFinishedRecipe {
        private final MetalPressTile.Mode mode;

        public Result(IRecipeSerializer<?> serializer, Ingredient ingredient, IRecipeResult result, ResourceLocation id, MetalPressTile.Mode mode) {
            super(serializer, ingredient, result, id);
            this.mode = mode;
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);
            json.addProperty("recipe", mode.name().toLowerCase());
        }
    }
}
