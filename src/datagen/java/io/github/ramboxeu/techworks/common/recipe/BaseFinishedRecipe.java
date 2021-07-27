package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public abstract class BaseFinishedRecipe implements IFinishedRecipe {
    protected final IRecipeSerializer<?> serializer;
    protected final Ingredient ingredient;
    protected final IRecipeResult result;
    protected final ResourceLocation id;

    public BaseFinishedRecipe(IRecipeSerializer<?> serializer, Ingredient ingredient, IRecipeResult result, ResourceLocation id) {
        this.serializer = serializer;
        this.ingredient = ingredient;
        this.result = result;
        this.id = id;
    }

    @Override
    public void serialize(JsonObject json) {
        json.add("ingredient", ingredient.serialize());

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", ForgeRegistries.ITEMS.getKey(result.getItem()).toString());

        if (result.getCount() > 1) {
            resultObj.addProperty("count", result.getCount());
        }

        json.add("result", resultObj);
    }

    @Override
    public ResourceLocation getID() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return serializer;
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
