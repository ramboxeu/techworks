package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class GrinderRecipeBuilder {
    private final Ingredient ingredient;
    private final IRecipeResult result;
    private final int energy;
    private final IRecipeSerializer<?> serializer;

    public GrinderRecipeBuilder(Ingredient ingredient, IRecipeResult result, int energy, IRecipeSerializer<?> serializer) {
        this.ingredient = ingredient;
        this.result = result;
        this.energy = energy;
        this.serializer = serializer;
    }

    public static GrinderRecipeBuilder grinding(Ingredient ingredient, IRecipeResult result, int energy) {
        return new GrinderRecipeBuilder(ingredient, result, energy, TechworksRecipes.GRINDING.getSerializer());
    }

    public static GrinderRecipeBuilder oreCrushing(Ingredient ingredient, IRecipeResult result, int energy) {
        return new GrinderRecipeBuilder(ingredient, result, energy, TechworksRecipes.ORE_CRUSHING.getSerializer());
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Result(serializer, ingredient, new ResourceLocation(Techworks.MOD_ID, name), result, energy));
    }

    private static class Result extends BaseFinishedRecipe {
        private final int energy;

        public Result(IRecipeSerializer<?> serializer, Ingredient ingredient, ResourceLocation id, IRecipeResult result, int energy) {
            super(serializer, ingredient, result, id);
            this.energy = energy;
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);
            json.addProperty("energy", energy);
        }
    }
}
