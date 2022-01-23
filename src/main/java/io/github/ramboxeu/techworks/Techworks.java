package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.client.TechworksClientEvents;
import io.github.ramboxeu.techworks.client.model.cable.CableModelLoader;
import io.github.ramboxeu.techworks.common.TechworksItemGroup;
import io.github.ramboxeu.techworks.common.debug.DebugInfoRenderer;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.recipe.SizedIngredient;
import io.github.ramboxeu.techworks.common.registration.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

        TechworksBlocks.BLOCKS.register(modEventBus);
        TechworksTiles.TILES.register(modEventBus);
        TechworksItems.ITEMS.register(modEventBus);
        TechworksContainers.CONTAINERS.register(modEventBus);
        TechworksFluids.FLUIDS.register(modEventBus);
        TechworksRecipes.RECIPES.register(modEventBus);
        TechworksComponents.TYPES.register(modEventBus);

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::onPreStitch);
        modEventBus.addListener(this::registerModelLoaders);
        modEventBus.addListener(this::makeRegistries);

        MinecraftForge.EVENT_BUS.register(TechworksEvents.class);
    }

    private void makeRegistries(RegistryEvent.NewRegistry event) {
        TechworksRegistries.register();
    }

    public void setup(FMLCommonSetupEvent event) {
        TechworksPacketHandler.register();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        TechworksTiles.bindRenderers();
        TechworksContainers.registerScreenFactories();

        RenderTypeLookup.setRenderLayer(TechworksBlocks.ITEM_TRANSPORTER.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(TechworksBlocks.LIQUID_PIPE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(TechworksBlocks.LIQUID_TANK.get(), RenderType.getCutout());

        MinecraftForge.EVENT_BUS.addListener(DebugInfoRenderer::render);
        MinecraftForge.EVENT_BUS.register(TechworksClientEvents.class);
    }

    public void onPreStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) return;

        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/gas_input"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/gas_output"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/gas_both"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/liquid_input"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/liquid_output"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "machine/port/liquid_both"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "white"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "block/port"));
        event.addSprite(new ResourceLocation(Techworks.MOD_ID, "block/port_new"));
    }

    public void registerModelLoaders(ModelRegistryEvent event) {
        LOGGER.debug("Registering model loader!");
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Techworks.MOD_ID, "cable"), new CableModelLoader());
    }

    @SubscribeEvent
    public void registerIngredientSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CraftingHelper.register(new ResourceLocation(MOD_ID, "sized"), SizedIngredient.Serializer.INSTANCE);
    }
}
