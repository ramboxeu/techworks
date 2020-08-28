package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.block.cable.BasicGasPipeBlock;
import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.gas.EmptyGas;
import io.github.ramboxeu.techworks.common.gas.Gas;
import io.github.ramboxeu.techworks.common.gas.SteamGas;
import io.github.ramboxeu.techworks.common.recipe.ElectricGrinderRecipe;
import io.github.ramboxeu.techworks.common.recipe.MachineAssemblyRecipe;
import io.github.ramboxeu.techworks.common.tile.cable.BasicGasPipeTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import static io.github.ramboxeu.techworks.Techworks.MOD_ID;

public class Registration {
    private static final IForgeRegistry<Gas> GAS_REGISTRY = new RegistryBuilder<Gas>().setName(new ResourceLocation(MOD_ID, "gas")).setType(Gas.class).create();

    private static final DeferredRegister<Block>             BLOCKS     = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item>              ITEMS      = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES      = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>>  CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
    private static final DeferredRegister<Gas>               GASES      = DeferredRegister.create(GAS_REGISTRY, MOD_ID);

    public static void addToEventBus() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GASES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BasicGasPipeBlock> GAS_PIPE_BASIC_BLOCK = BLOCKS.register("gas_pipe_basic", BasicGasPipeBlock::new);

    public static final RegistryObject<Item> GAS_PIPE_BASIC_ITEM = ITEMS.register("gas_pipe_basic", () -> new BlockItem(GAS_PIPE_BASIC_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<DebuggerItem> DEBUGGER_ITEM = ITEMS.register("debugger", DebuggerItem::new);

    public static final RegistryObject<TileEntityType<BasicGasPipeTile>> GAS_PIPE_BASIC_TILE = TILES.register("gas_pipe_basic", () ->  TileEntityType.Builder.create(BasicGasPipeTile::new, GAS_PIPE_BASIC_BLOCK.get()).build(null));

    public static final RegistryObject<EmptyGas> EMPTY_GAS = GASES.register("empty", EmptyGas::new);
    public static final RegistryObject<SteamGas> STEAM_GAS = GASES.register("steam", SteamGas::new);

    public static final IRecipeType<ElectricGrinderRecipe> GRINDING_RECIPE = IRecipeType.register("techworks:grinding");
    public static final IRecipeSerializer<ElectricGrinderRecipe> GRINDING_RECIPE_SERIALIZER = IRecipeSerializer.register("techworks:grinding", new ElectricGrinderRecipe.Serializer());
    public static final IRecipeType<MachineAssemblyRecipe> MACHINE_ASSEMBLY_RECIPE = IRecipeType.register(new ResourceLocation(Techworks.MOD_ID, "machine_assembly").toString());
    public static final IRecipeSerializer<MachineAssemblyRecipe> MACHINE_ASSEMBLY_RECIPE_SERIALIZER = IRecipeSerializer.register(new ResourceLocation(Techworks.MOD_ID, "machine_assembly").toString(), new MachineAssemblyRecipe.Serializer());

    public static Gas getGasByString(String name) {
        return GAS_REGISTRY.getValue(new ResourceLocation(name));
    }
}
