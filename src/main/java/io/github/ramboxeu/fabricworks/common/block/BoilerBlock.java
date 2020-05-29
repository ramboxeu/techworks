package io.github.ramboxeu.fabricworks.common.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class BoilerBlock extends Block {
    public BoilerBlock() {
        super(FabricBlockSettings.of(Material.METAL).build());
    }
}
