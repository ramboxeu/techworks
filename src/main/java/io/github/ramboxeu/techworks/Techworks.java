package io.github.ramboxeu.techworks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Techworks.MOD_ID)
public class Techworks {
    public static final String MOD_ID = "techworks";
    public static final Logger LOGGER = LogManager.getLogger("Techworks");

    @SidedProxy(serverSide = "io.github.ramboxeu.techworks.common.ServerProxy", clientSide = "io.github.ramboxeu.techworks.client.ClientProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("PreInitialization phase");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Initialization phase");
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("PostInitialization phase");
        proxy.postInit(event);
    }
}
