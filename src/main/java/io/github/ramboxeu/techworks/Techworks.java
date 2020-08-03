package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.client.model.CableModelLoader;
import io.github.ramboxeu.techworks.client.render.MachineTileEntityRenderer;
import io.github.ramboxeu.techworks.common.TechworksItemGroup;
import io.github.ramboxeu.techworks.common.capability.CapabilityExtendedListenerProvider;
import io.github.ramboxeu.techworks.common.debug.DebugInfoRenderer;
import io.github.ramboxeu.techworks.common.network.TechworkPacketHandler;
import io.github.ramboxeu.techworks.common.registration.*;
import io.github.ramboxeu.techworks.common.registry.BlockDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.TileDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.TileRegistryObject;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Techworks.MOD_ID)
public class Techworks {
    public static final String MOD_ID = "techworks";
    public static final Logger LOGGER = LogManager.getLogger("Techworks");

    public static final ItemGroup ITEM_GROUP = new TechworksItemGroup();

    public Techworks() {
        LOGGER.info("Starting up Techworks!");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Registration.addToEventBus();
        TechworksBlocks.BLOCKS.register(modEventBus);
        TechworksTiles.TILES.register(modEventBus);
        TechworksItems.ITEMS.register(modEventBus);
        TechworksContainers.CONTAINERS.register(modEventBus);
        TechworksFluids.addToEventBus();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::onPreStitch);

        MinecraftForge.EVENT_BUS.register(TechworksEvents.class);
    }

    public void setup(FMLCommonSetupEvent event) {
        CapabilityGas.register();
        CapabilityExtendedListenerProvider.register();
        TechworkPacketHandler.register();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation("techworks:cable"), new CableModelLoader());
        TechworksTiles.bindMachineRenderers();
        TechworksContainers.registerScreenFactories();

        MinecraftForge.EVENT_BUS.addListener(DebugInfoRenderer::render);
    }

    public void onPreStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) return;

        // Stitch all of this onto blocks atlas until creation of custom atlases is possible
        Techworks.LOGGER.debug("Stitching!");
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/gas_input"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/gas_output"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/gas_both"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/liquid_input"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/liquid_output"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/liquid_both"));
    }
}
