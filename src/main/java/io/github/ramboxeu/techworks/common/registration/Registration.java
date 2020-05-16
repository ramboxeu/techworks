package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.client.container.SteamEngineContainer;
import io.github.ramboxeu.techworks.client.screen.BoilerScreen;
import io.github.ramboxeu.techworks.client.screen.SteamEngineScreen;
import io.github.ramboxeu.techworks.common.block.BoilerBlock;
import io.github.ramboxeu.techworks.common.block.SteamEngineBlock;
import io.github.ramboxeu.techworks.common.block.cable.BasicGasPipeBlock;
import io.github.ramboxeu.techworks.common.debug.DebuggerItem;
import io.github.ramboxeu.techworks.common.gas.EmptyGas;
import io.github.ramboxeu.techworks.common.gas.Gas;
import io.github.ramboxeu.techworks.common.gas.SteamGas;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import io.github.ramboxeu.techworks.common.tile.BoilerTile;
import io.github.ramboxeu.techworks.common.tile.SteamEngineTile;
import io.github.ramboxeu.techworks.common.tile.cable.BasicGasPipeTile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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

    private static final DeferredRegister<Block>             BLOCKS     = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item>              ITEMS      = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES      = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>>  CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MOD_ID);
    private static final DeferredRegister<Gas>               GASES      = new DeferredRegister<>(GAS_REGISTRY, MOD_ID);

    public static void addToEventBus() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GASES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BoilerBlock> BOILER_BLOCK = BLOCKS.register("boiler", BoilerBlock::new);
    public static final RegistryObject<SteamEngineBlock> STEAM_ENGINE_BLOCK = BLOCKS.register("steam_engine", SteamEngineBlock::new);
    public static final RegistryObject<BasicGasPipeBlock> GAS_PIPE_BASIC_BLOCK = BLOCKS.register("gas_pipe_basic", BasicGasPipeBlock::new);

    public static final RegistryObject<Item> BOILER_ITEM =  ITEMS.register("boiler", () -> new BlockItem(BOILER_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<Item> STEAM_ENGINE_ITEM = ITEMS.register("steam_engine", () -> new BlockItem(STEAM_ENGINE_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<Item> GAS_PIPE_BASIC_ITEM = ITEMS.register("gas_pipe_basic", () -> new BlockItem(GAS_PIPE_BASIC_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<DebuggerItem> DEBUGGER_ITEM = ITEMS.register("debugger", DebuggerItem::new);

    public static final RegistryObject<TileEntityType<BoilerTile>> BOILER_TILE = TILES.register("boiler", () -> TileEntityType.Builder.create(BoilerTile::new, BOILER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<SteamEngineTile>> STEAM_ENGINE_TILE = TILES.register("steam_engine", () -> TileEntityType.Builder.create(SteamEngineTile::new, STEAM_ENGINE_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<BasicGasPipeTile>> GAS_PIPE_BASIC_TILE = TILES.register("gas_pipe_basic", () ->  TileEntityType.Builder.create(BasicGasPipeTile::new, GAS_PIPE_BASIC_BLOCK.get()).build(null));

    public static final RegistryObject<ContainerType<BoilerContainer>> BOILER_CONTAINER = CONTAINERS.register("boiler", () -> IForgeContainerType.create((id, playerInventory, buf) -> new BoilerContainer(id, playerInventory, getTileFromPacketBuffer(buf))));
    public static final RegistryObject<ContainerType<SteamEngineContainer>> STEAM_ENGINE_CONTAINER = CONTAINERS.register("steam_engine", () -> IForgeContainerType.create((id, inventory, buf) -> new SteamEngineContainer(id, inventory, getTileFromPacketBuffer(buf))));

    public static final RegistryObject<EmptyGas> EMPTY_GAS = GASES.register("empty", EmptyGas::new);
    public static final RegistryObject<SteamGas> STEAM_GAS = GASES.register("steam", SteamGas::new);

    // TODO: Make this NullPointerException safe
    private static AbstractMachineTile getTileFromPacketBuffer(PacketBuffer buffer) {
        return (AbstractMachineTile) Minecraft.getInstance().world.getTileEntity(buffer.readBlockPos());
    }

    public static void registerScreens(){
        ScreenManager.registerFactory(BOILER_CONTAINER.get(), BoilerScreen::new);
        ScreenManager.registerFactory(STEAM_ENGINE_CONTAINER.get(), SteamEngineScreen::new);
    }

    public static Gas getGasByString(String name) {
        return GAS_REGISTRY.getValue(new ResourceLocation(name));
    }
}
