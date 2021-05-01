package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.client.container.AssemblyTableContainer;
import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import io.github.ramboxeu.techworks.client.container.DevBlockContainer;
import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.client.container.machine.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.client.container.machine.ElectricGrinderContainer;
import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.client.screen.AssemblyTableScreen;
import io.github.ramboxeu.techworks.client.screen.BlueprintTableScreen;
import io.github.ramboxeu.techworks.client.screen.DevBlockScreen;
import io.github.ramboxeu.techworks.client.screen.machine.BoilerScreen;
import io.github.ramboxeu.techworks.client.screen.machine.ElectricFurnaceScreen;
import io.github.ramboxeu.techworks.client.screen.machine.ElectricGrinderScreen;
import io.github.ramboxeu.techworks.client.screen.machine.SteamEngineScreen;
import io.github.ramboxeu.techworks.common.registry.ContainerDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.ContainerRegistryObject;
import io.github.ramboxeu.techworks.common.tile.AssemblyTableTile;
import io.github.ramboxeu.techworks.common.tile.BlueprintTableTile;
import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import net.minecraft.client.gui.ScreenManager;

public class TechworksContainers {
    public static final ContainerDeferredRegister CONTAINERS = new ContainerDeferredRegister();

    public static final ContainerRegistryObject<BoilerContainer> BOILER = CONTAINERS.registerMachineContainer("boiler", BoilerContainer::new);
    public static final ContainerRegistryObject<SteamEngineContainer> STEAM_ENGINE = CONTAINERS.registerMachineContainer("steam_engine", SteamEngineContainer::new);
    public static final ContainerRegistryObject<ElectricGrinderContainer> ELECTRIC_GRINDER = CONTAINERS.registerMachineContainer("electric_grinder", ElectricGrinderContainer::new);
    public static final ContainerRegistryObject<ElectricFurnaceContainer> ELECTRIC_FURNACE = CONTAINERS.<ElectricFurnaceTile, ElectricFurnaceContainer>registerMachineContainer("electric_furnace", ElectricFurnaceContainer::new); // Java for some reason won't infer types on its own

    public static final ContainerRegistryObject<BlueprintTableContainer> BLUEPRINT_TABLE = CONTAINERS.registerTileContainer("blueprint_table", (id, inv, tile) -> new BlueprintTableContainer(id, inv, (BlueprintTableTile) tile));
    public static final ContainerRegistryObject<AssemblyTableContainer> ASSEMBLY_TABLE = CONTAINERS.registerTileContainer("assembly_table", (id, inv, tile) -> new AssemblyTableContainer(id, inv, (AssemblyTableTile) tile));
    public static final ContainerRegistryObject<DevBlockContainer> DEV_BLOCK = CONTAINERS.registerTileContainer("dev_block", (id, inv, tile) -> new DevBlockContainer(id, (DevBlockTile) tile));

    public static void registerScreenFactories(){
        ScreenManager.registerFactory(BOILER.get(), BoilerScreen::new);
        ScreenManager.registerFactory(STEAM_ENGINE.get(), SteamEngineScreen::new);
        ScreenManager.registerFactory(ELECTRIC_GRINDER.get(), ElectricGrinderScreen::new);
        ScreenManager.registerFactory(ELECTRIC_FURNACE.get(), ElectricFurnaceScreen::new);
        ScreenManager.registerFactory(BLUEPRINT_TABLE.get(), BlueprintTableScreen::new);
        ScreenManager.registerFactory(ASSEMBLY_TABLE.get(), AssemblyTableScreen::new);
        ScreenManager.registerFactory(DEV_BLOCK.get(), DevBlockScreen::new);
    }
}
