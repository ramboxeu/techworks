package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.client.render.*;
import io.github.ramboxeu.techworks.common.registry.TileDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.TileRegistryObject;
import io.github.ramboxeu.techworks.common.tile.*;
import io.github.ramboxeu.techworks.common.tile.machine.*;
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
    public static final TileRegistryObject<SolidFuelBurnerTile> SOLID_FUEL_BURNER = TILES.register(TechworksBlocks.SOLID_FUEL_BURNER, SolidFuelBurnerTile::new);
    public static final TileRegistryObject<OreWasherTile> ORE_WASHER = TILES.register(TechworksBlocks.ORE_WASHER, OreWasherTile::new);
    public static final TileRegistryObject<MetalPressTile> METAL_PRESS = TILES.register(TechworksBlocks.METAL_PRESS, MetalPressTile::new);
    public static final TileRegistryObject<IndustrialFurnaceTile> INDUSTRIAL_FURNACE = TILES.register(TechworksBlocks.INDUSTRIAL_FURNACE, IndustrialFurnaceTile::new);
    public static final TileRegistryObject<LiquidTankTile> LIQUID_STORAGE = TILES.register(TechworksBlocks.LIQUID_TANK, LiquidTankTile::new);
    public static final TileRegistryObject<GasTankTile> GAS_STORAGE = TILES.register(TechworksBlocks.GAS_TANK, GasTankTile::new);
    public static final TileRegistryObject<EnergyStorageTile> ENERGY_STORAGE = TILES.register(TechworksBlocks.ENERGY_STORAGE, EnergyStorageTile::new);
    public static final TileRegistryObject<LiquidPumpTile> LIQUID_PUMP = TILES.register(TechworksBlocks.LIQUID_PUMP, LiquidPumpTile::new);
    public static final TileRegistryObject<OreMinerTile> ORE_MINER_TILE = TILES.register(TechworksBlocks.ORE_MINER, OreMinerTile::new);
    public static final TileRegistryObject<AnvilIngotHolderTile> ANVIL_INGOT_HOLDER = TILES.register(TechworksBlocks.ANVIL_INGOT_HOLDER, AnvilIngotHolderTile::new);

    public static final TileRegistryObject<DevBlockTile> DEV_BLOCK = TILES.register(TechworksBlocks.DEV_BLOCK, DevBlockTile::new);

    public static void bindRenderers() {
//        ClientRegistry.bindTileEntityRenderer(BOILER.getTileType(), MachineIOTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(STEAM_ENGINE.getTileType(), MachineIOTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(ELECTRIC_GRINDER.getTileType(), MachineIOTileEntityRenderer::new);
//        ClientRegistry.bindTileEntityRenderer(ELECTRIC_FURNACE.get(), MachineIOTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ITEM_TRANSPORTER.get(), CableTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(LIQUID_STORAGE.get(), LiquidStorageRenderer.tileRenderer());
        ClientRegistry.bindTileEntityRenderer(GAS_STORAGE.get(), GasStorageRenderer.tileRenderer());
        ClientRegistry.bindTileEntityRenderer(ENERGY_STORAGE.get(), EnergyStorageRenderer.tileRenderer());
//        ClientRegistry.bindTileEntityRenderer(CREATIVE_ENERGY_BATTERY.getTileType(), MachineIOTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ANVIL_INGOT_HOLDER.get(), AnvilIngotHolderTileRenderer::new);
    }
}
