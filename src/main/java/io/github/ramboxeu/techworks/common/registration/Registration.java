package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.gas.Gas;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.client.screen.BoilerScreen;
import io.github.ramboxeu.techworks.common.block.BoilerBlock;
import io.github.ramboxeu.techworks.common.tile.AbstractMachineTile;
import io.github.ramboxeu.techworks.common.tile.BoilerTile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

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
    public static final RegistryObject<Item> BOILER_ITEM =  ITEMS.register("boiler", () -> new BlockItem(BOILER_BLOCK.get(), new Item.Properties().group(Techworks.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BoilerTile>> BOILER_TILE = TILES.register("boiler", () -> TileEntityType.Builder.create(BoilerTile::new, BOILER_BLOCK.get()).build(null));
    public static final RegistryObject<ContainerType<BoilerContainer>> BOILER_CONTAINER = CONTAINERS.register("boiler", () -> IForgeContainerType.create((id, playerInventory, buf) -> new BoilerContainer(id, playerInventory, getTileFromPacketBuffer(buf))));

    // TODO: Make this NullPointerException safe
    private static AbstractMachineTile getTileFromPacketBuffer(PacketBuffer buffer) {
        return (AbstractMachineTile) Minecraft.getInstance().world.getTileEntity(buffer.readBlockPos());
    }

    public static void registerScreens(){
        ScreenManager.registerFactory(BOILER_CONTAINER.get(), BoilerScreen::new);
    }
}
