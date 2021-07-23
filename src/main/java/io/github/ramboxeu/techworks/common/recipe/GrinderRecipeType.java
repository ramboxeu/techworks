package io.github.ramboxeu.techworks.common.recipe;

import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import net.minecraft.item.crafting.IRecipeType;

public enum GrinderRecipeType {
    GRINDING(TechworksRecipes.GRINDING.get()),
    ORE_CRUSHING(TechworksRecipes.ORE_CRUSHING.get());

    private final IRecipeType<? extends IGrinderRecipe> type;

    GrinderRecipeType(IRecipeType<? extends IGrinderRecipe> type) {
        this.type = type;
    }

    public IRecipeType<? extends IGrinderRecipe> get() {
        return type;
    }
}
