package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.Techworks;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.util.Identifier;

public class TechworksScreens {
    public static Identifier BOILER;

    private static <T extends Container> Identifier register(String name, Factory<T> factory) {
        Identifier identifier = new Identifier(Techworks.MOD_ID, name);
        ScreenProviderRegistry.INSTANCE.registerFactory(identifier, factory::create);

        return identifier;
    }

    public static void registerAll() {
        BOILER = register("boiler", BoilerScreen::new);
    }

    private interface Factory<T extends Container> {
        ContainerScreen<T> create(T container);
    }
}
