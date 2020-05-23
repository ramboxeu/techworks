package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ElectricGrinderRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final String group;

    public ElectricGrinderRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.group = group;
    }


    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Registration.GRINDING_RECIPE_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return Registration.GRINDING_RECIPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ElectricGrinderRecipe> {
        public Serializer() {
            setRegistryName(new ResourceLocation(Techworks.MOD_ID, "grinding"));
        }

        @Override
        public ElectricGrinderRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));

            if (!json.has("result")) {
                throw new JsonSyntaxException("Missing result, expected to find a string or object");
            }

            ItemStack result;
            if (json.get("result").isJsonObject()){
                result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            } else {
                result = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, "result"))));
            }
            return new ElectricGrinderRecipe(recipeId, group, ingredient, result);
        }

        @Override
        public ElectricGrinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString();
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            return new ElectricGrinderRecipe(recipeId, group, ingredient, result);
        }

        @Override
        public void write(PacketBuffer buffer, ElectricGrinderRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}
