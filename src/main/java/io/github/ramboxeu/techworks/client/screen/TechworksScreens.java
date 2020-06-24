package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.registry.TechworksContainers;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

// FIXME: This all has to be re-done
public class TechworksScreens {
//    private static <T extends ScreenHandler> HandledScreen<T> register(String name, Factory<T> factory) {
////        Identifier identifier = new Identifier(Techworks.MOD_ID, name);
////        ScreenRegistry.register(identifier, factory::create);
////
////        return identifier;
//    }

    public static void registerAll() {
        ScreenRegistry.<BoilerContainer, BoilerScreen>register(TechworksContainers.BOILER, (handler, inventory, title) -> new BoilerScreen(handler));
    }

    private interface Factory<T extends ScreenHandler> {
        HandledScreen<T> create(T container);
    }
}
