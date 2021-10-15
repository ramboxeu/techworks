package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.tile.machine.MetalPressTile;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class MetalPressingRecipe extends BaseProcessingRecipe {
    private final MetalPressTile.Mode mode;

    public MetalPressingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, MetalPressTile.Mode mode) {
        super(id, ingredient, output);
        this.mode = mode;
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

    public static class Serializer extends BaseProcessingRecipeSerializer<MetalPressingRecipe> {

        @Override
        protected MetalPressingRecipe readRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, JsonObject json) {
            MetalPressTile.Mode mode = JsonUtils.readEnum(json, "recipe", MetalPressTile.Mode.class);
            return new MetalPressingRecipe(id, ingredient, output, mode);
        }

        @Override
        protected MetalPressingRecipe readRecipe(ResourceLocation id, Ingredient ingredient, ItemStack output, PacketBuffer buf) {
            MetalPressTile.Mode mode = buf.readEnumValue(MetalPressTile.Mode.class);
            return new MetalPressingRecipe(id, ingredient, output, mode);
        }

        @Override
        public void write(PacketBuffer buf, MetalPressingRecipe recipe) {
            super.write(buf, recipe);
            buf.writeEnumValue(recipe.mode);
        }
    }
}
