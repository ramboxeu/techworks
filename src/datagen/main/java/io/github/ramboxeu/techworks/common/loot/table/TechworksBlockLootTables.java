package io.github.ramboxeu.techworks.common.loot.table;

import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootTable;

import java.util.HashSet;
import java.util.Set;

public class TechworksBlockLootTables extends BlockLootTables {
    private final Set<Block> knownBlocks =  new HashSet<>();

    @Override
    protected void registerLootTable(Block block, LootTable.Builder table) {
        super.registerLootTable(block, table);
        knownBlocks.add(block);
    }

    @Override
    public Set<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void addTables() {
        registerDropSelfLootTable(Registration.BOILER_BLOCK.get());
        registerDropSelfLootTable(Registration.ELECTRIC_FURNACE_BLOCK.get());
        registerDropSelfLootTable(Registration.ELECTRIC_GRINDER_BLOCK.get());
    }
}
