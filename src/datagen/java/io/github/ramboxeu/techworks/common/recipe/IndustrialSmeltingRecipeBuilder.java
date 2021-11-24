package io.github.ramboxeu.techworks.common.recipe;

import com.google.common.collect.ImmutableList;
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
import java.util.List;
import java.util.function.Consumer;

public class IndustrialSmeltingRecipeBuilder {
    private final List<Ingredient> ingredients;
    private final IRecipeResult result;
    private final int temperature;
    private final int heat;

    public IndustrialSmeltingRecipeBuilder(List<Ingredient> ingredients, IRecipeResult result, int temperature, int heat) {
        this.ingredients = ingredients;
        this.result = result;
        this.temperature = temperature;
        this.heat = heat;
    }

    public static IndustrialSmeltingRecipeBuilder smelting(Ingredient ingredient, IRecipeResult result, int temperature, int heat) {
        return new IndustrialSmeltingRecipeBuilder(ImmutableList.of(ingredient), result, temperature, heat);
    }

    public static IndustrialSmeltingRecipeBuilder smelting(Ingredient ingredient1, Ingredient ingredient2, IRecipeResult result, int temperature, int heat) {
        return new IndustrialSmeltingRecipeBuilder(ImmutableList.of(ingredient1, ingredient2), result, temperature, heat);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Result(new ResourceLocation(Techworks.MOD_ID, name), ingredients, result, temperature, heat));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final List<Ingredient> ingredients;
        private final IRecipeResult result;
        private final int temperature;
        private final int heat;

        public Result(ResourceLocation id, List<Ingredient> ingredients, IRecipeResult result, int temperature, int heat) {
            this.id = id;
            this.ingredients = ingredients;
            this.result = result;
            this.temperature = temperature;
            this.heat = heat;
        }

        @Override
        public void serialize(JsonObject json) {
            JsonArray ingredientsJson = new JsonArray();

            for (Ingredient ingredient : ingredients) {
                ingredientsJson.add(ingredient.serialize());
            }
            json.add("ingredients", ingredientsJson);

            JsonObject resultObj = new JsonObject();
            resultObj.addProperty("item", ForgeRegistries.ITEMS.getKey(result.getItem()).toString());

            if (result.getCount() > 1) {
                resultObj.addProperty("count", result.getCount());
            }

            json.add("result", resultObj);

            json.addProperty("temperature", temperature);
            json.addProperty("heat", heat);
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return TechworksRecipes.INDUSTRIAL_SMELTING.getSerializer();
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
