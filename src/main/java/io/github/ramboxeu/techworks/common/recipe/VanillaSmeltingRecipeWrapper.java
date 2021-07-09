package io.github.ramboxeu.techworks.common.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class VanillaSmeltingRecipeWrapper implements ITechworksSmeltingRecipe {
    private final AbstractCookingRecipe delegate;

    public VanillaSmeltingRecipeWrapper(AbstractCookingRecipe delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getEnergy() {
        return TechworksSmeltingRecipe.ENERGY;
    }

    @Override
    public int getCookTime() {
        return 0;
    }

    @Override
    public float getExperience() {
        return delegate.getExperience();
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return delegate.matches(inv, world);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return delegate.getCraftingResult(inv);
    }

    @Override
    public boolean canFit(int width, int height) {
        return delegate.canFit(width, height);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return delegate.getRecipeOutput();
    }

    @Override
    public ResourceLocation getId() {
        return delegate.getId();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return delegate.getSerializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return delegate.getType();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        return delegate.getRemainingItems(inv);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return delegate.getIngredients();
    }

    @Override
    public boolean isDynamic() {
        return delegate.isDynamic();
    }

    @Override
    public String getGroup() {
        return delegate.getGroup();
    }

    @Override
    public ItemStack getIcon() {
        return delegate.getIcon();
    }
}
