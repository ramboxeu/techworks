package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.AssemblyTableContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.container.BlueprintTableContainer;
import io.github.ramboxeu.techworks.client.container.machine.*;
import io.github.ramboxeu.techworks.client.screen.*;
import io.github.ramboxeu.techworks.common.block.AssemblyTableBlock;
import io.github.ramboxeu.techworks.common.component.IComponentsContainerProvider;
import io.github.ramboxeu.techworks.common.registry.ContainerDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.ContainerRegistryObject;
import io.github.ramboxeu.techworks.common.tile.AssemblyTableTile;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.tile.BlueprintTableTile;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TechworksContainers {
    public static final ContainerDeferredRegister CONTAINERS = new ContainerDeferredRegister();

    public static final ContainerRegistryObject<BoilerContainer> BOILER = CONTAINERS.registerMachineContainer("boiler", BoilerContainer::new);
    public static final ContainerRegistryObject<SteamEngineContainer> STEAM_ENGINE = CONTAINERS.registerMachineContainer("steam_engine", SteamEngineContainer::new);
    public static final ContainerRegistryObject<ElectricGrinderContainer> ELECTRIC_GRINDER = CONTAINERS.registerMachineContainer("electric_grinder", ElectricGrinderContainer::new);
    public static final ContainerRegistryObject<ElectricFurnaceContainer> ELECTRIC_FURNACE = CONTAINERS.registerMachineContainer("electric_furnace", ElectricFurnaceContainer::new);

    public static final ContainerRegistryObject<ComponentsContainer> COMPONENTS = CONTAINERS.register("components", (id, inv, buf) -> {
                ComponentStackHandler components = ComponentStackHandler.empty();

                BlockPos pos = buf.readBlockPos();
                TileEntity te = inv.player.world.getTileEntity(pos);

                if (te instanceof BaseMachineTile) {
                    components = ((BaseMachineTile) te).getComponents();
                } else {
                    // Should this crash, or something?
                    Techworks.LOGGER.error("Expected BaseMachineTile on {}, but it was not found!", pos);
                }

                return new ComponentsContainer(id, inv, components);
            });

    public static final ContainerRegistryObject<BlueprintTableContainer> BLUEPRINT_TABLE = CONTAINERS.registerTileContainer("blueprint_table", (id, inv, tile) -> new BlueprintTableContainer(id, inv, (BlueprintTableTile) tile));
    public static final ContainerRegistryObject<AssemblyTableContainer> ASSEMBLY_TABLE = CONTAINERS.registerTileContainer("assembly_table", (id, inv, tile) -> new AssemblyTableContainer(id, inv, (AssemblyTableTile) tile));

    public static void registerScreenFactories(){
        ScreenManager.registerFactory(BOILER.getContainer(), BoilerScreen::new);
        ScreenManager.registerFactory(STEAM_ENGINE.getContainer(), SteamEngineScreen::new);
        ScreenManager.registerFactory(ELECTRIC_GRINDER.getContainer(), ElectricGrinderScreen::new);
        ScreenManager.registerFactory(ELECTRIC_FURNACE.getContainer(), ElectricFurnaceScreen::new);
        ScreenManager.registerFactory(COMPONENTS.getContainer(), ComponentsScreen::new);
        ScreenManager.registerFactory(BLUEPRINT_TABLE.getContainer(), BlueprintTableScreen::new);
        ScreenManager.registerFactory(ASSEMBLY_TABLE.getContainer(), AssemblyTableScreen::new);
    }
}
