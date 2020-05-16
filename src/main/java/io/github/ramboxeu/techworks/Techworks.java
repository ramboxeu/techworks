package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.client.model.CableModelLoader;
import io.github.ramboxeu.techworks.common.TechworksItemGroup;
import io.github.ramboxeu.techworks.common.debug.DebugInfoRenderer;
import io.github.ramboxeu.techworks.common.network.TechworkPacketHandler;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
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

        Registration.addToEventBus();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(DebugInfoRenderer::render);
    }

    public void setup(FMLCommonSetupEvent event) {
        CapabilityGas.register();
        TechworkPacketHandler.register();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        Registration.registerScreens();
        ModelLoaderRegistry.registerLoader(new ResourceLocation("techworks:cable"), new CableModelLoader());
    }
}
