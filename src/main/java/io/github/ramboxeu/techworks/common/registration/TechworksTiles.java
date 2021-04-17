package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.client.render.CableTileEntityRenderer;
import io.github.ramboxeu.techworks.client.render.MachineIOTileEntityRenderer;
import io.github.ramboxeu.techworks.common.registry.TileDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.TileRegistryObject;
import io.github.ramboxeu.techworks.common.tile.*;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricGrinderTile;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import io.github.ramboxeu.techworks.common.util.cable.network.NetworkType;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TechworksTiles {
    public static final TileDeferredRegister TILES = new TileDeferredRegister();

    public static final TileRegistryObject<BoilerTile> BOILER = TILES.register(TechworksBlocks.BOILER, BoilerTile::new);
    public static final TileRegistryObject<SteamEngineTile> STEAM_ENGINE = TILES.register(TechworksBlocks.STEAM_ENGINE, SteamEngineTile::new);
    public static final TileRegistryObject<ElectricGrinderTile> ELECTRIC_GRINDER = TILES.register(TechworksBlocks.ELECTRIC_GRINDER, ElectricGrinderTile::new);
    public static final TileRegistryObject<ElectricFurnaceTile> ELECTRIC_FURNACE = TILES.register(TechworksBlocks.ELECTRIC_FURNACE, ElectricFurnaceTile::new);

    public static final TileRegistryObject<BlueprintTableTile> BLUEPRINT_TABLE = TILES.register(TechworksBlocks.BLUEPRINT_TABLE, BlueprintTableTile::new);
    public static final TileRegistryObject<AssemblyTableTile> ASSEMBLY_TABLE = TILES.register(TechworksBlocks.ASSEMBLY_TABLE, AssemblyTableTile::new);

    public static final TileRegistryObject<CreativeEnergyBatteryTile> CREATIVE_ENERGY_BATTERY = TILES.register(TechworksBlocks.CREATIVE_ENERGY_BATTERY, CreativeEnergyBatteryTile::new);

    public static final TileRegistryObject<ItemTransporterTile> ITEM_TRANSPORTER = TILES.register(TechworksBlocks.ITEM_TRANSPORTER, ItemTransporterTile::new);
    public static final TileRegistryObject<FluidPipeTile> LIQUID_PIPE = TILES.register(TechworksBlocks.LIQUID_PIPE, () -> new FluidPipeTile(TechworksTiles.LIQUID_PIPE, NetworkType.LIQUID));
    public static final TileRegistryObject<FluidPipeTile> GAS_PIPE = TILES.register(TechworksBlocks.GAS_PIPE, () -> new FluidPipeTile(TechworksTiles.GAS_PIPE, NetworkType.GAS));
    public static final TileRegistryObject<EnergyCableTile> ENERGY_CABLE = TILES.register(TechworksBlocks.ENERGY_CABLE, EnergyCableTile::new);

    public static final TileRegistryObject<DevBlockTile> DEV_BLOCK = TILES.register(TechworksBlocks.DEV_BLOCK, DevBlockTile::new);

    public static void bindRenderers() {
//        ClientRegistry.bindTileEntityRenderer(BOILER.getTileType(), MachineIOTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(STEAM_ENGINE.getTileType(), MachineIOTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(ELECTRIC_GRINDER.getTileType(), MachineIOTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ELECTRIC_FURNACE.get(), MachineIOTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITEM_TRANSPORTER.get(), CableTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(CREATIVE_ENERGY_BATTERY.getTileType(), MachineIOTileEntityRenderer::new);
    }
}
