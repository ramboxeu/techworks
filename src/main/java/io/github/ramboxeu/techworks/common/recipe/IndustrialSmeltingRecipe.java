package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IndustrialSmeltingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final List<SizedIngredient> ingredients;
    private final ItemStack result;
    private final int temperature;
    private final int heat;

    public IndustrialSmeltingRecipe(ResourceLocation id, List<SizedIngredient> ingredients, ItemStack result, int temperature, int heat) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.temperature = temperature;
        this.heat = heat;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        for (int i = 0, size = inv.getSizeInventory(); i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

//            if (stack.isEmpty())
//                continue;

            if (ingredients.stream().noneMatch(ingredient -> ingredient.test(stack)))
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        List<SizedIngredient> ingredients = new ArrayList<>(this.ingredients);

        for (Iterator<SizedIngredient> iter = ingredients.iterator(); iter.hasNext(); ) {
            SizedIngredient ingredient = iter.next();

            for (int i = 0, size = inv.getSizeInventory(); i < size; i++) {
                ItemStack stack = inv.getStackInSlot(i);
                int count = ingredient.getCount(stack);

                if (count > 0) {
                    stack.shrink(count);
                    iter.remove();
                    break;
                }
            }
        }

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
        return TechworksRecipes.INDUSTRIAL_SMELTING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.INDUSTRIAL_SMELTING.get();
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHeat() {
        return heat;
    }

    public int getIngredientsCount() {
        return ingredients.size();
    }

    public static class Serializer extends BaseRecipeSerializer<IndustrialSmeltingRecipe> {

        @Override
        public IndustrialSmeltingRecipe read(ResourceLocation recipeId, JsonObject json) {
            JsonArray ingredientsJson = JSONUtils.getJsonArray(json, "ingredients");

            if (ingredientsJson.size() > 2)
                throw new JsonSyntaxException("Expected 'ingredients' to have no more than 2 elements");

            List<SizedIngredient> ingredients = new ArrayList<>(ingredientsJson.size());
            for (JsonElement element : ingredientsJson) {
                if (!element.isJsonObject())
                    throw new JsonSyntaxException("Expected 'ingredients' elements to objects");

                ingredients.add(SizedIngredient.deserialize(element.getAsJsonObject()));
            }

            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int temperature = JSONUtils.getInt(json, "temperature");
            int heat = JSONUtils.getInt(json, "heat");

            return new IndustrialSmeltingRecipe(recipeId, ingredients, result, temperature, heat);
        }

        @Nullable
        @Override
        public IndustrialSmeltingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int size = buffer.readByte();
            List<SizedIngredient> ingredients = new ArrayList<>(size);

            for (int i = 0; i < size; i++) {
                ingredients.add(SizedIngredient.read(buffer));
            }

            ItemStack result = buffer.readItemStack();
            int temperature = buffer.readVarInt();
            int heat = buffer.readVarInt();

            return new IndustrialSmeltingRecipe(recipeId, ingredients, result, temperature, heat);
        }

        @Override
        public void write(PacketBuffer buffer, IndustrialSmeltingRecipe recipe) {
            buffer.writeByte(recipe.ingredients.size());

            for (SizedIngredient ingredient : recipe.ingredients) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.result);
            buffer.writeVarInt(recipe.temperature);
            buffer.writeVarInt(recipe.heat);
        }
    }
}
