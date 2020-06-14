package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.registry.*;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Techworks implements ModInitializer {
    public static final String MOD_ID = "techworks";
    public static final Logger LOG = LogManager.getLogger("Techworks");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Techworks for Fabric!");
        TechworksBlocks.registerAll();
        TechworksItems.registerAll();
        TechworksBlockEntities.registerAll();
        TechworksContainers.registerAll();
        ComponentTypes.registerAll();
        ComponentProviders.registerAll();
    }
}
