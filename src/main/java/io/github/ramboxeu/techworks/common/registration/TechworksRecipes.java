package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.recipe.MachineAssemblyRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public class TechworksRecipes {
    public static final IRecipeType<MachineAssemblyRecipe> MACHINE_ASSEMBLY_RECIPE = IRecipeType.register(new ResourceLocation(Techworks.MOD_ID, "machine_assembly").toString());
    public static final IRecipeSerializer<MachineAssemblyRecipe> MACHINE_ASSEMBLY_RECIPE_SERIALIZER = IRecipeSerializer.register(new ResourceLocation(Techworks.MOD_ID, "machine_assembly").toString(), new MachineAssemblyRecipe.Serializer());
}
