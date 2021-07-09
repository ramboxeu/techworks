package io.github.ramboxeu.techworks.common.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface ITechworksSmeltingRecipe extends IRecipe<IInventory> {
    int getEnergy();
    @Deprecated
    int getCookTime();
    float getExperience();
}
