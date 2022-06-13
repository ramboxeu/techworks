package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WireCuttingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private final SizedIngredient ingredient;
    private final ItemStack output;

    public WireCuttingRecipe(ResourceLocation recipeId, SizedIngredient ingredient, ItemStack output) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.output = output;
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
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return TechworksRecipes.WIRE_CUTTING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.WIRE_CUTTING.get();
    }

    public int getIngredientCount(ItemStack stack) {
        return ingredient.getCount(stack);
    }

    public static class Serializer extends BaseRecipeSerializer<WireCuttingRecipe> {

        @Override
        public WireCuttingRecipe read(ResourceLocation recipeId, JsonObject json) {
            SizedIngredient ingredient = SizedIngredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));

            return new WireCuttingRecipe(recipeId, ingredient, result);
        }

        @Nullable
        @Override
        public WireCuttingRecipe read(ResourceLocation recipeId, PacketBuffer buf) {
            SizedIngredient ingredient = SizedIngredient.read(buf);
            ItemStack result = buf.readItemStack();

            return new WireCuttingRecipe(recipeId, ingredient, result);
        }

        @Override
        public void write(PacketBuffer buf, WireCuttingRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeItemStack(recipe.output);
        }
    }
}
