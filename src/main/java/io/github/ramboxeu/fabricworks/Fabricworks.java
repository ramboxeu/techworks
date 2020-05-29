package io.github.ramboxeu.fabricworks;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Fabricworks implements ModInitializer {
    public static final Logger LOG = LogManager.getLogger("Fabricworks");

    @Override
    public void onInitialize() {
        LOG.info("Initializing fabricworks!");
    }
}
