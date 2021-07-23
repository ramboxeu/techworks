package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.recipe.GrindingRecipe;
import io.github.ramboxeu.techworks.common.recipe.MachineAssemblyRecipe;
import io.github.ramboxeu.techworks.common.recipe.OreCrushingRecipe;
import io.github.ramboxeu.techworks.common.recipe.TechworksSmeltingRecipe;
import io.github.ramboxeu.techworks.common.registry.RecipeDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.RecipeRegistryObject;

public class TechworksRecipes {
    public static final RecipeDeferredRegister RECIPES = new RecipeDeferredRegister();

    public static final RecipeRegistryObject<MachineAssemblyRecipe, MachineAssemblyRecipe.Serializer> MACHINE_ASSEMBLY = RECIPES.register("machine_assembly", MachineAssemblyRecipe.Serializer::new);
    public static final RecipeRegistryObject<GrindingRecipe, GrindingRecipe.Serializer> GRINDING = RECIPES.register("grinding", GrindingRecipe.Serializer::new);
    public static final RecipeRegistryObject<TechworksSmeltingRecipe, TechworksSmeltingRecipe.Serializer> SMELTING = RECIPES.register("smelting", TechworksSmeltingRecipe.Serializer::new);
    public static final RecipeRegistryObject<OreCrushingRecipe, OreCrushingRecipe.Serializer> ORE_CRUSHING = RECIPES.register("ore_crushing", OreCrushingRecipe.Serializer::new);
}
