package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.client.render.MachineTileEntityRenderer;
import io.github.ramboxeu.techworks.common.registry.TileDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.TileRegistryObject;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricFurnaceTile;
import io.github.ramboxeu.techworks.common.tile.machine.ElectricGrinderTile;
import io.github.ramboxeu.techworks.common.tile.machine.SteamEngineTile;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TechworksTiles {
    public static final TileDeferredRegister TILES = new TileDeferredRegister();

    public static final TileRegistryObject<BoilerTile> BOILER = TILES.register(TechworksBlocks.BOILER, BoilerTile::new);
    public static final TileRegistryObject<SteamEngineTile> STEAM_ENGINE = TILES.register(TechworksBlocks.STEAM_ENGINE, SteamEngineTile::new);
    public static final TileRegistryObject<ElectricGrinderTile> ELECTRIC_GRINDER = TILES.register(TechworksBlocks.ELECTRIC_GRINDER, ElectricGrinderTile::new);
    public static final TileRegistryObject<ElectricFurnaceTile> ELECTRIC_FURNACE = TILES.register(TechworksBlocks.ELECTRIC_FURNACE, ElectricFurnaceTile::new);

    public static void bindMachineRenderers() {
        ClientRegistry.bindTileEntityRenderer(BOILER.getTileType(), MachineTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(STEAM_ENGINE.getTileType(), MachineTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ELECTRIC_GRINDER.getTileType(), MachineTileEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ELECTRIC_FURNACE.getTileType(), MachineTileEntityRenderer::new);
    }
}
