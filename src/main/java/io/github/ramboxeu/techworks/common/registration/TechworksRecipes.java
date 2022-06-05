package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.recipe.*;
import io.github.ramboxeu.techworks.common.registry.RecipeDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.RecipeRegistryObject;

public class TechworksRecipes {
    public static final RecipeDeferredRegister RECIPES = new RecipeDeferredRegister();

    public static final RecipeRegistryObject<MachineAssemblyRecipe, MachineAssemblyRecipe.Serializer> MACHINE_ASSEMBLY = RECIPES.register("machine_assembly", MachineAssemblyRecipe.Serializer::new);
    public static final RecipeRegistryObject<GrindingRecipe, GrindingRecipe.Serializer> GRINDING = RECIPES.register("grinding", GrindingRecipe.Serializer::new);
    public static final RecipeRegistryObject<TechworksSmeltingRecipe, TechworksSmeltingRecipe.Serializer> SMELTING = RECIPES.register("smelting", TechworksSmeltingRecipe.Serializer::new);
    public static final RecipeRegistryObject<OreCrushingRecipe, OreCrushingRecipe.Serializer> ORE_CRUSHING = RECIPES.register("ore_crushing", OreCrushingRecipe.Serializer::new);
    public static final RecipeRegistryObject<OreWashingRecipe, OreWashingRecipe.Serializer> ORE_WASHING = RECIPES.register("ore_washing", OreWashingRecipe.Serializer::new);
    public static final RecipeRegistryObject<MetalPressingRecipe, MetalPressingRecipe.Serializer> METAL_PRESSING = RECIPES.register("metal_pressing", MetalPressingRecipe.Serializer::new);
    public static final RecipeRegistryObject<IndustrialSmeltingRecipe, IndustrialSmeltingRecipe.Serializer> INDUSTRIAL_SMELTING = RECIPES.register("industrial_smelting", IndustrialSmeltingRecipe.Serializer::new);
    public static final RecipeRegistryObject<HammeringRecipe, HammeringRecipe.Serializer> HAMMERING = RECIPES.register("hammering", HammeringRecipe.Serializer::new);
}
