package io.github.ramboxeu.techworks.common.loot.table;

import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
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
        for (BlockRegistryObject<?, ?> block : DataConstants.Blocks.DROPPING_SELF) {
            registerDropSelfLootTable(block.getBlock());
        }
    }
}
