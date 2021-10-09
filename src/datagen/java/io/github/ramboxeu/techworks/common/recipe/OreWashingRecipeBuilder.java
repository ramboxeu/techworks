package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OreWashingRecipeBuilder {
    private final Ingredient ingredient;
    private final List<ResultBuilder> resultBuilders;

    public OreWashingRecipeBuilder(Ingredient ingredient) {
        this.ingredient = ingredient;

        resultBuilders = new ArrayList<>(4);
    }

    public OreWashingRecipeBuilder(Ingredient ingredient, List<ResultBuilder> resultBuilders) {
        this.ingredient = ingredient;

        if (resultBuilders.size() >= 4) throw new IllegalStateException("Too many results, 4 is max");
        this.resultBuilders = resultBuilders;
    }

    public OreWashingRecipeBuilder result(ResultBuilder builder) {
        if (resultBuilders.size() >= 4) throw new IllegalStateException("Too many results, 4 is max");
        resultBuilders.add(builder);
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Recipe(ingredient, buildResults(), new ResourceLocation(Techworks.MOD_ID, name)));
    }

    private List<Result> buildResults() {
        return resultBuilders.stream().map(ResultBuilder::build).collect(Collectors.toList());
    }

    public static class ResultBuilder {
        private final IRecipeResult result;
        private int maxCount = 1;
        private int minCount = 1;
        private int chance = 100;

        public ResultBuilder(IRecipeResult result) {
            this.result = result;
        }

        public ResultBuilder count(int min, int max) {
            if (min > max) throw new IllegalArgumentException("Min greater than max");

            maxCount = max;
            minCount = min;
            return this;
        }

        public ResultBuilder count(int count) {
            maxCount = count;
            minCount = count;
            return this;
        }

        public ResultBuilder chance(int chance) {
            if (chance < 1 || chance > 100) throw new IllegalArgumentException("Chance is not in range [1, 100]");

            this.chance = chance;
            return this;
        }

        public IRecipeResult getResult() {
            return result;
        }

        private Result build() {
            return new Result(result, maxCount, minCount, chance);
        }
    }

    public static class Result {
        private final IRecipeResult result;
        private final int max;
        private final int min;
        private final int chance;

        public Result(IRecipeResult result, int max, int min, int chance) {
            this.result = result;
            this.max = max;
            this.min = min;
            this.chance = chance;
        }
    }

    public static class Recipe implements IFinishedRecipe {
        private final Ingredient ingredient;
        private final List<Result> results;
        private final ResourceLocation id;

        public Recipe(Ingredient ingredient, List<Result> results, ResourceLocation id) {
            this.ingredient = ingredient;
            this.results = results;
            this.id = id;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("ingredient", ingredient.serialize());

            JsonArray resultsArray = new JsonArray();

            for (Result result : results) {
                JsonObject resultObj = new JsonObject();

                JsonObject recipeResultObj = new JsonObject();
                IRecipeResult recipeResult = result.result;
                recipeResultObj.addProperty("item", ForgeRegistries.ITEMS.getKey(recipeResult.getItem()).toString());

                if (recipeResult.getCount() > 1) {
                    recipeResultObj.addProperty("count", recipeResult.getCount());
                }

                resultObj.add("result", recipeResultObj);
                resultObj.addProperty("max", result.max);
                resultObj.addProperty("min", result.min);
                resultObj.addProperty("chance", result.chance);

                resultsArray.add(resultObj);
            }

            json.add("results", resultsArray);
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return TechworksRecipes.ORE_WASHING.getSerializer();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
