package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.TechworksItemGroup;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.item.ItemGroup;
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
    }

    public void setup(FMLCommonSetupEvent event) {

    }

    public void clientSetup(FMLClientSetupEvent event) {
        Registration.registerScreens();
    }
}
