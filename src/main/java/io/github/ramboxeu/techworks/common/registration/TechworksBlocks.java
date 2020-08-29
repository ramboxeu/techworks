package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.common.block.*;
import io.github.ramboxeu.techworks.common.registry.BlockDeferredRegister;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import net.minecraft.item.BlockItem;

import java.util.ArrayList;
import java.util.List;

public class TechworksBlocks {
    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister();

    public static final List<BlockRegistryObject<? extends BaseMachineBlock, BlockItem>> MACHINES = new ArrayList<>();

    public static final BlockRegistryObject<BoilerBlock, BlockItem> BOILER = BLOCKS.register("boiler", BoilerBlock::new);
    public static final BlockRegistryObject<SteamEngineBlock, BlockItem> STEAM_ENGINE = BLOCKS.register("steam_engine", SteamEngineBlock::new);
    public static final BlockRegistryObject<ElectricGrinderBlock, BlockItem> ELECTRIC_GRINDER = BLOCKS.register("electric_grinder", ElectricGrinderBlock::new);
    public static final BlockRegistryObject<ElectricFurnaceBlock, BlockItem> ELECTRIC_FURNACE = BLOCKS.register("electric_furnace", ElectricFurnaceBlock::new);
    public static final BlockRegistryObject<AssemblyTableBlock, BlockItem> ASSEMBLY_TABLE = BLOCKS.register("assembly_table", AssemblyTableBlock::new);

    public static final BlockRegistryObject<BlueprintTableBlock, BlockItem> BLUEPRINT_TABLE = BLOCKS.register("blueprint_table", BlueprintTableBlock::new);

    public static final BlockRegistryObject<CreativeEnergyBatteryBlock, BlockItem> CREATIVE_ENERGY_BATTERY = BLOCKS.register("creative_battery", CreativeEnergyBatteryBlock::new);

    static {
        MACHINES.add(BOILER);
        MACHINES.add(STEAM_ENGINE);
        MACHINES.add(ELECTRIC_GRINDER);
        MACHINES.add(ELECTRIC_FURNACE);
    }
}
