package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class GrindingRecipe extends BaseProcessingRecipe implements IGrinderRecipe {
    public static final int ENERGY = 2000;

    private final String group;
    private final int energy;

    public GrindingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, int energy) {
        super(id, ingredient, result);
        this.group = group;
        this.energy = energy;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return TechworksRecipes.GRINDING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.GRINDING.get();
    }

    public static class Serializer extends BaseRecipeSerializer<GrindingRecipe> {

        @Override
        public GrindingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int energy = JSONUtils.getInt(json, "energy", ENERGY);

            return new GrindingRecipe(recipeId, group, ingredient, result, energy);
        }

        @Override
        public GrindingRecipe read(ResourceLocation recipeId, PacketBuffer buf) {
            ResourceLocation id = buf.readResourceLocation();
            String group = buf.readString();
            Ingredient ingredient = Ingredient.read(buf);
            ItemStack result = buf.readItemStack();
            int energy = buf.readInt();

            return new GrindingRecipe(id, group, ingredient, result, energy);
        }

        @Override
        public void write(PacketBuffer buffer, GrindingRecipe recipe) {
            buffer.writeResourceLocation(recipe.id);
            buffer.writeString(recipe.group);
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeInt(recipe.energy);
        }
    }
}
