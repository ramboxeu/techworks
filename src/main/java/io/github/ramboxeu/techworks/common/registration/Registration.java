package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.client.container.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.client.container.ElectricGrinderContainer;
import io.github.ramboxeu.techworks.client.container.SteamEngineContainer;
import io.github.ramboxeu.techworks.client.screen.BoilerScreen;
import io.github.ramboxeu.techworks.client.screen.ElectricFurnaceScreen;
import io.github.ramboxeu.techworks.client.screen.ElectricGrinderScreen;
import io.github.ramboxeu.techworks.client.screen.SteamEngineScreen;
import io.github.ramboxeu.techworks.common.block.BoilerBlock;
import io.github.ramboxeu.techworks.common.block.ElectricFurnaceBlock;
import io.github.ramboxeu.techworks.common.block.ElectricGrinderBlock;
import io.github.ramboxeu.techworks.common.block.SteamEngineBlock;
import io.github.ramboxeu.techworks.common.block.cable.BasicGasPipeBlock;
import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.gas.EmptyGas;
import io.github.ramboxeu.techworks.common.gas.Gas;
import io.github.ramboxeu.techworks.common.gas.SteamGas;
import io.github.ramboxeu.techworks.common.item.GroundItem;
import io.github.ramboxeu.techworks.common.recipe.ElectricGrinderRecipe;
import io.github.ramboxeu.techworks.common.tile.*;
import io.github.ramboxeu.techworks.common.tile.cable.BasicGasPipeTile;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricGrinderTile;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
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

    public static final RegistryObject<BoilerBlock> BOILER_BLOCK = BLOCKS.register("boiler", BoilerBlock::new);
    public static final RegistryObject<SteamEngineBlock> STEAM_ENGINE_BLOCK = BLOCKS.register("steam_engine", SteamEngineBlock::new);
    public static final RegistryObject<ElectricGrinderBlock> ELECTRIC_GRINDER_BLOCK = BLOCKS.register("electric_grinder", ElectricGrinderBlock::new);
    public static final RegistryObject<ElectricFurnaceBlock> ELECTRIC_FURNACE_BLOCK = BLOCKS.register("electric_furnace", ElectricFurnaceBlock::new);
    public static final RegistryObject<BasicGasPipeBlock> GAS_PIPE_BASIC_BLOCK = BLOCKS.register("gas_pipe_basic", BasicGasPipeBlock::new);

    public static final RegistryObject<Item> BOILER_ITEM =  ITEMS.register("boiler", () -> new BlockItem(BOILER_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<Item> STEAM_ENGINE_ITEM = ITEMS.register("steam_engine", () -> new BlockItem(STEAM_ENGINE_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<Item> ELECTRIC_GRINDER_ITEM = ITEMS.register("electric_grinder", () -> new BlockItem(ELECTRIC_GRINDER_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<Item> ELECTRIC_FURNACE_ITEM = ITEMS.register("electric_furnace", () -> new BlockItem(ELECTRIC_FURNACE_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<Item> GAS_PIPE_BASIC_ITEM = ITEMS.register("gas_pipe_basic", () -> new BlockItem(GAS_PIPE_BASIC_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<DebuggerItem> DEBUGGER_ITEM = ITEMS.register("debugger", DebuggerItem::new);
    public static final RegistryObject<GroundItem> GROUND_IRON_ITEM = ITEMS.register("ground_iron", GroundItem::new);
    public static final RegistryObject<GroundItem> GROUND_GOLD_ITEM = ITEMS.register("ground_gold", GroundItem::new);

    public static final RegistryObject<TileEntityType<BoilerTile>> BOILER_TILE = TILES.register("boiler", () -> TileEntityType.Builder.create(BoilerTile::new, BOILER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<SteamEngineTile>> STEAM_ENGINE_TILE = TILES.register("steam_engine", () -> TileEntityType.Builder.create(SteamEngineTile::new, STEAM_ENGINE_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<ElectricGrinderTile>> ELECTRIC_GRINDER_TILE = TILES.register("electric_grinder", () -> TileEntityType.Builder.create(ElectricGrinderTile::new, ELECTRIC_GRINDER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<ElectricFurnaceTile>> ELECTRIC_FURNACE_TILE = TILES.register("electric_furnace", () -> TileEntityType.Builder.create(ElectricFurnaceTile::new, ELECTRIC_FURNACE_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<BasicGasPipeTile>> GAS_PIPE_BASIC_TILE = TILES.register("gas_pipe_basic", () ->  TileEntityType.Builder.create(BasicGasPipeTile::new, GAS_PIPE_BASIC_BLOCK.get()).build(null));

    public static final RegistryObject<ContainerType<BoilerContainer>> BOILER_CONTAINER = CONTAINERS.register("boiler", () -> IForgeContainerType.create((id, playerInventory, buf) -> new BoilerContainer(id, playerInventory, getTileFromPacketBuffer(buf))));
    public static final RegistryObject<ContainerType<SteamEngineContainer>> STEAM_ENGINE_CONTAINER = CONTAINERS.register("steam_engine", () -> IForgeContainerType.create((id, inventory, buf) -> new SteamEngineContainer(id, inventory, getTileFromPacketBuffer(buf))));
    public static final RegistryObject<ContainerType<ElectricGrinderContainer>> ELECTRIC_GRINDER_CONTAINER = CONTAINERS.register("electric_grinder", () -> IForgeContainerType.create((id, inventory, buf) -> new ElectricGrinderContainer(id, inventory, getTileFromPacketBuffer(buf))));
    public static final RegistryObject<ContainerType<ElectricFurnaceContainer>> ELECTRIC_FURNACE_CONTAINER = CONTAINERS.register("electric_furnace", () -> IForgeContainerType.create((id, inventory, buf) -> new ElectricFurnaceContainer(id, inventory, getTileFromPacketBuffer(buf))));

    public static final RegistryObject<EmptyGas> EMPTY_GAS = GASES.register("empty", EmptyGas::new);
    public static final RegistryObject<SteamGas> STEAM_GAS = GASES.register("steam", SteamGas::new);

    public static final IRecipeType<ElectricGrinderRecipe> GRINDING_RECIPE = IRecipeType.register("techworks:grinding");
    public static final IRecipeSerializer<ElectricGrinderRecipe> GRINDING_RECIPE_SERIALIZER = IRecipeSerializer.register("techworks:grinding", new ElectricGrinderRecipe.Serializer());

    // TODO: Make this NullPointerException safe
    private static BaseMachineTile getTileFromPacketBuffer(PacketBuffer buffer) {
        return (BaseMachineTile) Minecraft.getInstance().world.getTileEntity(buffer.readBlockPos());
    }

    public static void registerScreens(){
        ScreenManager.registerFactory(BOILER_CONTAINER.get(), BoilerScreen::new);
        ScreenManager.registerFactory(STEAM_ENGINE_CONTAINER.get(), SteamEngineScreen::new);
        ScreenManager.registerFactory(ELECTRIC_GRINDER_CONTAINER.get(), ElectricGrinderScreen::new);
        ScreenManager.registerFactory(ELECTRIC_FURNACE_CONTAINER.get(), ElectricFurnaceScreen::new);
    }

    public static Gas getGasByString(String name) {
        return GAS_REGISTRY.getValue(new ResourceLocation(name));
    }
}
