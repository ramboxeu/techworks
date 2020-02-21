package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class TechworksBlock extends Block {
    public TechworksBlock(Material blockMaterialIn, MapColor blockMapColorIn, String registryName, String unlocalizedName) {
        super(blockMaterialIn, blockMapColorIn);

        setRegistryName(Techworks.MOD_ID, registryName);
        setUnlocalizedName(Techworks.MOD_ID + "." + unlocalizedName);

        TechworksBlocks.BLOCKS.add(this);
        TechworksBlocks.ITEM_BLOCKS.add(new ItemBlock(this).setRegistryName(registryName));
    }
}
