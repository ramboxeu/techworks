package io.github.ramboxeu.techworks.common.recipe;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class WireCuttingRecipeBuilder {
    private final Ingredient ingredient;
    private final IRecipeResult result;

    private WireCuttingRecipeBuilder(Ingredient ingredient, IRecipeResult result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    public static WireCuttingRecipeBuilder wireCutting(Ingredient ingredient, IRecipeResult result) {
        return new WireCuttingRecipeBuilder(ingredient, result);
    }

    public void build(Consumer<IFinishedRecipe> consumer, String name) {
        consumer.accept(new Result(ingredient, result, new ResourceLocation(Techworks.MOD_ID, name)));
    }

    public static class Result extends BaseFinishedRecipe {

        public Result(Ingredient ingredient, IRecipeResult result, ResourceLocation id) {
            super(TechworksRecipes.WIRE_CUTTING.getSerializer(), ingredient, result, id);
        }
    }
}
