package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.recipe.ElectricGrinderRecipe;
import io.github.ramboxeu.techworks.common.recipe.MachineAssemblyRecipe;
import io.github.ramboxeu.techworks.common.recipe.TechworksSmeltingRecipe;
import io.github.ramboxeu.techworks.common.registry.RecipeDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.RecipeRegistryObject;

public class TechworksRecipes {
    public static final RecipeDeferredRegister RECIPES = new RecipeDeferredRegister();

    public static final RecipeRegistryObject<MachineAssemblyRecipe, MachineAssemblyRecipe.Serializer> MACHINE_ASSEMBLY = RECIPES.register("machine_assembly", MachineAssemblyRecipe.Serializer::new);
    public static final RecipeRegistryObject<ElectricGrinderRecipe, ElectricGrinderRecipe.Serializer> GRINDING = RECIPES.register("grinding", ElectricGrinderRecipe.Serializer::new);
    public static final RecipeRegistryObject<TechworksSmeltingRecipe, TechworksSmeltingRecipe.Serializer> SMELTING = RECIPES.register("smelting", TechworksSmeltingRecipe.Serializer::new);
}
