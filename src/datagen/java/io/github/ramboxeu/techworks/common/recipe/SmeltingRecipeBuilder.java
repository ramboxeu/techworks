package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class SmeltingRecipeBuilder {
    private final Ingredient ingredient;
    private final IRecipeResult result;
    private final float experience;
    private final int energy;

    public SmeltingRecipeBuilder(Ingredient ingredient, IRecipeResult result, float experience, int energy) {
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.energy = energy;
    }

    public static SmeltingRecipeBuilder smelting(Ingredient ingredient, IRecipeResult result, float experience, int energy) {
        return new SmeltingRecipeBuilder(ingredient, result, experience, energy);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Result(ingredient, result, new ResourceLocation(Techworks.MOD_ID, name), experience, energy));
    }

    private static class Result extends BaseFinishedRecipe {
        private final float experience;
        private final int energy;

        public Result(Ingredient ingredient, IRecipeResult result, ResourceLocation id, float experience, int energy) {
            super(TechworksRecipes.SMELTING.getSerializer(), ingredient, result, id);
            this.experience = experience;
            this.energy = energy;
        }

        @Override
        public void serialize(JsonObject json) {
            super.serialize(json);

            json.addProperty("energy", energy);
            json.addProperty("experience", experience);
        }
    }
}
