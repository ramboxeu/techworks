package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class OreWashingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final Result[] results;
    private final List<ItemStack> outputs;

    public OreWashingRecipe(ResourceLocation id, Ingredient ingredient, Result[] results) {
        this.id = id;
        this.ingredient = ingredient;
        this.results = results;

        outputs = Arrays.stream(results).map(Result::getMaxStack).collect(Collectors.toList());
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return ingredient.test(inv.getStackInSlot(0));
    }

    @Override // Use getCraftingResults
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override // Use getRecipeOutputs
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public List<ItemStack> getRecipeOutputs() {
        return outputs;
    }

    public List<ItemStack> getCraftingResults(IItemHandler inv, Random random) {
        return Arrays.stream(results).map(r -> r.getRandomizedStack(random)).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return TechworksRecipes.ORE_WASHING.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return TechworksRecipes.ORE_WASHING.get();
    }

    public static class Serializer extends BaseRecipeSerializer<OreWashingRecipe> {

        @Override
        public OreWashingRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            JsonArray resultsArray = JSONUtils.getJsonArray(json, "results");

            if (resultsArray.size() >= 4) throw new IllegalArgumentException("More than 4 results are present");

            Result[] results = new Result[resultsArray.size()];
            int counter = 0;

            for (JsonElement resultJson : resultsArray) {
                JsonObject resultObj = resultJson.getAsJsonObject();

                ItemStack stack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(resultObj, "result"));
                int max = JSONUtils.getInt(resultObj, "max");
                int min = JSONUtils.getInt(resultObj, "min");
                if (min > max) throw new IllegalArgumentException("Min stack size is greater than max");

                int chance = JSONUtils.getInt(resultObj, "chance");
                if (chance < 1 || chance > 100) throw new IllegalArgumentException("Chance is not in range [1, 100]");

                results[counter++] = new Result(stack, min, max, chance);
            }

            return new OreWashingRecipe(recipeId, ingredient, results);
        }

        @Nullable
        @Override
        public OreWashingRecipe read(ResourceLocation recipeId, PacketBuffer buf) {
            Ingredient ingredient = Ingredient.read(buf);

            int size = buf.readByte();
            Result[] results = new Result[size];

            for (int i = 0; i < size; i++) {
                ItemStack stack = buf.readItemStack();
                int max = buf.readInt();
                int min = buf.readInt();
                int chance = buf.readInt();
                results[i] = new Result(stack, max, min, chance);
            }

            return new OreWashingRecipe(recipeId, ingredient, results);
        }

        @Override
        public void write(PacketBuffer buf, OreWashingRecipe recipe) {
            recipe.ingredient.write(buf);

            Result[] results = recipe.results;
            buf.writeByte(results.length);

            for (Result result : results) {
                buf.writeItemStack(result.stack);
                buf.writeInt(result.max);
                buf.writeInt(result.min);
                buf.writeInt(result.chance);
            }
        }
    }

    public static class Result {
        private final ItemStack stack;
        private final int min;
        private final int max;
        private final int chance;

        public Result(ItemStack stack, int min, int max, int chance) {
            this.min = min;
            this.max = max;
            this.chance = chance;

            if (stack.getCount() < max) {
                ItemStack copy = stack.copy();
                copy.setCount(max);
                this.stack = copy;
            } else {
                this.stack = stack;
            }
        }

        public ItemStack getMaxStack() {
            return stack;
        }

        public ItemStack getRandomizedStack(Random random) {
            int dropRoll = random.nextInt(100) + 1;

            if (dropRoll <= chance) {
                int countRoll = random.nextInt((max - min) + 1) + min;
                ItemStack output = stack.copy();
                output.setCount(countRoll);
                return output;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }
}
