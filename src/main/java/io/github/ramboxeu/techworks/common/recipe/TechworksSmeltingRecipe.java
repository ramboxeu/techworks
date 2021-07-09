package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;


public class TechworksSmeltingRecipe implements ITechworksSmeltingRecipe {
    public static final int ENERGY = 2000;

    private final ResourceLocation id;
    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int energy;

    public TechworksSmeltingRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int energy) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
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
        return TechworksRecipes.SMELTING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.SMELTING.get();
    }

    public int getEnergy() {
        return energy;
    }

    public int getCookTime() {
        return 0;
    }

    public float getExperience() {
        return experience;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TechworksSmeltingRecipe> {

        @Override
        public TechworksSmeltingRecipe read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int energy = JSONUtils.getInt(json, "energy", ENERGY);
            float experience = JSONUtils.getFloat(json, "experience", 0);

            return new TechworksSmeltingRecipe(recipeId, group, ingredient, result, experience, energy);
        }

        @Nullable
        @Override
        public TechworksSmeltingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(32767);
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            int energy = buffer.readVarInt();
            float experience = buffer.readFloat();
            return new TechworksSmeltingRecipe(recipeId, group, ingredient, result, experience, energy);
        }

        @Override
        public void write(PacketBuffer buffer, TechworksSmeltingRecipe recipe) {
            buffer.writeString(recipe.group);
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeVarInt(recipe.energy);
            buffer.writeFloat(recipe.experience);
        }
    }
}
