package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class BaseProcessingRecipeSerializer<T extends BaseProcessingRecipe> extends BaseRecipeSerializer<T> {

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
        ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        return readRecipe(recipeId, ingredient, result, json);
    }

    protected abstract T readRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, JsonObject json);

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buf) {
        Ingredient ingredient = Ingredient.read(buf);
        ItemStack result = buf.readItemStack();
        return readRecipe(recipeId, ingredient, result, buf);
    }

    protected abstract T readRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, PacketBuffer buf);

    @Override
    public void write(PacketBuffer buf, T recipe) {
        recipe.ingredient.write(buf);
        buf.writeItemStack(recipe.output);
    }
}
