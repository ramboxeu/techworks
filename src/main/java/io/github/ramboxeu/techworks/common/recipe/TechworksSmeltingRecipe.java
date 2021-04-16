package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

// Custom smelting recipe, allows energy consumption adjustments
public class TechworksSmeltingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int cookTime;
    private final int energy;

    public TechworksSmeltingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime, int energy) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.cookTime = cookTime;
        this.energy = energy;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Registration.TECHWORKS_SMELTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return Registration.TECHWORKS_SMELTING_RECIPE;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCookTime() {
        return cookTime;
    }

    public float getExperience() {
        return experience;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TechworksSmeltingRecipe> {

        @Override
        public TechworksSmeltingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            // TODO: make a better method to deserialize results (allow item ids)
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int cookTime = JSONUtils.getInt(json, "cookTime", 150);
            int energy = JSONUtils.getInt(json, "energy", 150);
            float experience = JSONUtils.getFloat(json, "experience", 0);

            return new TechworksSmeltingRecipe(recipeId, group, ingredient, result, experience, cookTime, energy);
        }

        @Nullable
        @Override
        public TechworksSmeltingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            int energy = buffer.readVarInt();
            int cookTime = buffer.readVarInt();
            float experience = buffer.readFloat();
            return new TechworksSmeltingRecipe(recipeId, group, ingredient, result, experience, cookTime, energy);
        }

        @Override
        public void write(PacketBuffer buffer, TechworksSmeltingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeVarInt(recipe.energy);
            buffer.writeVarInt(recipe.cookTime);
            buffer.writeFloat(recipe.experience);
        }
    }
}
