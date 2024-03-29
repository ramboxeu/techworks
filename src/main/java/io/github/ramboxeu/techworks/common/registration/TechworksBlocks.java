package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.client.render.EnergyStorageRenderer;
import io.github.ramboxeu.techworks.client.render.GasStorageRenderer;
import io.github.ramboxeu.techworks.client.render.LiquidStorageRenderer;
import io.github.ramboxeu.techworks.common.block.*;
import io.github.ramboxeu.techworks.common.registry.BlockDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class TechworksBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister();

    public static final BlockRegistryObject<BoilerBlock, BlockItem> BOILER = BLOCKS.register("boiler", BoilerBlock::new);
    public static final BlockRegistryObject<SteamEngineBlock, BlockItem> STEAM_ENGINE = BLOCKS.register("steam_engine", SteamEngineBlock::new);
    public static final BlockRegistryObject<ElectricGrinderBlock, BlockItem> ELECTRIC_GRINDER = BLOCKS.register("electric_grinder", ElectricGrinderBlock::new);
    public static final BlockRegistryObject<ElectricFurnaceBlock, BlockItem> ELECTRIC_FURNACE = BLOCKS.register("electric_furnace", ElectricFurnaceBlock::new);
    public static final BlockRegistryObject<AssemblyTableBlock, BlockItem> ASSEMBLY_TABLE = BLOCKS.register("assembly_table", AssemblyTableBlock::new);

    public static final BlockRegistryObject<BlueprintTableBlock, BlockItem> BLUEPRINT_TABLE = BLOCKS.register("blueprint_table", BlueprintTableBlock::new);

    public static final BlockRegistryObject<CreativeEnergyBatteryBlock, BlockItem> CREATIVE_ENERGY_BATTERY = BLOCKS.register("creative_battery", CreativeEnergyBatteryBlock::new);

    public static final BlockRegistryObject<CableBlock, BlockItem> ITEM_TRANSPORTER = BLOCKS.register("item_transporter", () -> new CableBlock(TechworksTiles.ITEM_TRANSPORTER));
    public static final BlockRegistryObject<CableBlock, BlockItem> LIQUID_PIPE = BLOCKS.register("liquid_pipe", () -> new CableBlock(TechworksTiles.LIQUID_PIPE));
    public static final BlockRegistryObject<CableBlock, BlockItem> GAS_PIPE = BLOCKS.register("gas_pipe", () -> new CableBlock(TechworksTiles.GAS_PIPE));
    public static final BlockRegistryObject<CableBlock, BlockItem> ENERGY_CABLE = BLOCKS.register("energy_cable", () -> new CableBlock(TechworksTiles.ENERGY_CABLE));
    public static final BlockRegistryObject<SolidFuelBurnerBlock, BlockItem> SOLID_FUEL_BURNER = BLOCKS.register("solid_fuel_burner", SolidFuelBurnerBlock::new);
    public static final BlockRegistryObject<Block, BlockItem> COPPER_ORE = BLOCKS.register("copper_ore", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3)));
    public static final BlockRegistryObject<Block, BlockItem> LITHIUM_ORE = BLOCKS.register("lithium_ore", () -> new Block(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3)));
    public static final BlockRegistryObject<OreWasherBlock, BlockItem> ORE_WASHER = BLOCKS.register("ore_washer", OreWasherBlock::new);
    public static final BlockRegistryObject<MetalPressBlock, BlockItem> METAL_PRESS = BLOCKS.register("metal_press", MetalPressBlock::new);
    public static final BlockRegistryObject<IndustrialFurnaceBlock, BlockItem> INDUSTRIAL_FURNACE = BLOCKS.register("industrial_furnace", IndustrialFurnaceBlock::new);
    public static final BlockRegistryObject<LiquidTankBlock, BlockItem> LIQUID_TANK = BLOCKS.register("liquid_tank", LiquidTankBlock::new, (block, props) -> new BlockItem(block, props.setISTER(() -> LiquidStorageRenderer::stackRenderer)));
    public static final BlockRegistryObject<GasTankBlock, BlockItem> GAS_TANK = BLOCKS.register("gas_tank", GasTankBlock::new, (block, props) -> new BlockItem(block, props.setISTER(() -> GasStorageRenderer::stackRenderer)));
    public static final BlockRegistryObject<EnergyStorageBlock, BlockItem> ENERGY_STORAGE = BLOCKS.register("energy_storage", EnergyStorageBlock::new, (block, props) -> new BlockItem(block, props.setISTER(() -> EnergyStorageRenderer::stackRenderer)));
    public static final BlockRegistryObject<LiquidPumpBlock, BlockItem> LIQUID_PUMP = BLOCKS.register("liquid_pump", LiquidPumpBlock::new);
    public static final BlockRegistryObject<OreMinerBlock, BlockItem> ORE_MINER = BLOCKS.register("ore_miner", OreMinerBlock::new);
    public static final BlockRegistryObject<AnvilIngotHolderBlock, BlockItem> ANVIL_INGOT_HOLDER = BLOCKS.register("anvil_ingot_holder", AnvilIngotHolderBlock::new, (block, props) -> new BlockItem(block, new Item.Properties()));

    // More testing
    public static final BlockRegistryObject<DevBlockBlock, BlockItem> DEV_BLOCK = BLOCKS.register("dev_block", DevBlockBlock::new, (block, props) -> new BlockItem(block, new Item.Properties()));
}
