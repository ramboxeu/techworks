package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.jetbrains.annotations.Nullable;

public class MetalPressingRecipe extends BaseProcessingRecipe {
    private final MetalPressTile.Mode mode;

    public MetalPressingRecipe(ResourceLocation id, SizedIngredient ingredient, ItemStack output, MetalPressTile.Mode mode) {
        super(id, ingredient, output);
        this.mode = mode;
    }
    public int getCount(ItemStack stack) {
        return ((SizedIngredient) ingredient).getCount(stack);
    }

    public MetalPressTile.Mode getMode() {
        return mode;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return TechworksRecipes.METAL_PRESSING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.METAL_PRESSING.get();
    }

    public static class Serializer extends BaseRecipeSerializer<MetalPressingRecipe> {

        @Override
        public MetalPressingRecipe read(ResourceLocation recipeId, JsonObject json) {
            SizedIngredient ingredient = SizedIngredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            MetalPressTile.Mode mode = JsonUtils.readEnum(json, "recipe", MetalPressTile.Mode.class);
            return new MetalPressingRecipe(recipeId, ingredient, result, mode);
        }

        @Nullable
        @Override
        public MetalPressingRecipe read(ResourceLocation recipeId, PacketBuffer buf) {
            SizedIngredient ingredient = SizedIngredient.read(buf);
            ItemStack result = buf.readItemStack();
            MetalPressTile.Mode mode = buf.readEnumValue(MetalPressTile.Mode.class);
            return new MetalPressingRecipe(recipeId, ingredient, result, mode);
        }

        @Override
        public void write(PacketBuffer buf, MetalPressingRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeItemStack(recipe.output);
            buf.writeEnumValue(recipe.mode);
        }
    }
}
