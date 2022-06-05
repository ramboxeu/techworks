package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class HammeringRecipeBuilder {
    private final Ingredient ingredient;
    private final IRecipeResult result;
    private final int hits;

    public HammeringRecipeBuilder(Ingredient ingredient, IRecipeResult result, int hits) {
        this.ingredient = ingredient;
        this.result = result;
        this.hits = hits;
    }

    public static HammeringRecipeBuilder hammering(Ingredient ingredient, IRecipeResult result, int hits) {
        return new HammeringRecipeBuilder(ingredient, result, hits);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Result(ingredient, result, new ResourceLocation(Techworks.MOD_ID, name), hits));
    }

    public static class Result extends BaseFinishedRecipe {
        private final int hits;

        public Result(Ingredient ingredient, IRecipeResult result, ResourceLocation id, int hits) {
            super(TechworksRecipes.HAMMERING.getSerializer(), ingredient, result, id);
            this.hits = hits;
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);
            json.addProperty("hits", hits);
        }
    }
}
