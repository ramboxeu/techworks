package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.recipe.ElectricGrinderRecipe;
import io.github.ramboxeu.techworks.common.recipe.MachineAssemblyRecipe;
import io.github.ramboxeu.techworks.common.recipe.TechworksSmeltingRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static io.github.ramboxeu.techworks.Techworks.MOD_ID;

public class Registration {
    private static final DeferredRegister<Item>              ITEMS      = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    private static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static void addToEventBus() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<DebuggerItem> DEBUGGER_ITEM = ITEMS.register("debugger", DebuggerItem::new);

    public static final IRecipeType<ElectricGrinderRecipe> GRINDING_RECIPE = IRecipeType.register("techworks:grinding");
    public static final RegistryObject<IRecipeSerializer<ElectricGrinderRecipe>> GRINDING_RECIPE_SERIALIZER = SERIALIZERS.register("grinding", ElectricGrinderRecipe.Serializer::new);
    public static final IRecipeType<MachineAssemblyRecipe> MACHINE_ASSEMBLY_RECIPE = IRecipeType.register(new ResourceLocation(Techworks.MOD_ID, "machine_assembly").toString());
    public static final RegistryObject<IRecipeSerializer<MachineAssemblyRecipe>> MACHINE_ASSEMBLY_RECIPE_SERIALIZER = SERIALIZERS.register("machine_assembly", MachineAssemblyRecipe.Serializer::new);
    public static final IRecipeType<TechworksSmeltingRecipe> TECHWORKS_SMELTING_RECIPE = IRecipeType.register(new ResourceLocation(Techworks.MOD_ID, "smelting").toString());
    public static final RegistryObject<IRecipeSerializer<TechworksSmeltingRecipe>> TECHWORKS_SMELTING_RECIPE_SERIALIZER = SERIALIZERS.register("smelting", TechworksSmeltingRecipe.Serializer::new);
}
