package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HammeringRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final SizedIngredient ingredient;
    private final ItemStack output;
    private final int hits;

    public HammeringRecipe(ResourceLocation id, SizedIngredient ingredient, ItemStack output, int hits) {
        this.id = id;
        this.hits = hits;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return TechworksRecipes.HAMMERING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.HAMMERING.get();
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public int getHits() {
        return hits;
    }

    public SizedIngredient getIngredient() {
        return ingredient;
    }

    public static class Serializer extends BaseRecipeSerializer<HammeringRecipe> {

        @Override
        public HammeringRecipe read(ResourceLocation recipeId, JsonObject json) {
            SizedIngredient ingredient = SizedIngredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int hits = JSONUtils.getInt(json, "hits");

            return new HammeringRecipe(recipeId, ingredient, result, hits);
        }

        @Nullable
        @Override
        public HammeringRecipe read(ResourceLocation recipeId, PacketBuffer buf) {
            SizedIngredient ingredient = SizedIngredient.read(buf);
            ItemStack result = buf.readItemStack();
            int hits = buf.readInt();

            return new HammeringRecipe(recipeId, ingredient, result, hits);
        }

        @Override
        public void write(PacketBuffer buf, HammeringRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeItemStack(recipe.output);
            buf.writeInt(recipe.hits);
        }
    }
}
