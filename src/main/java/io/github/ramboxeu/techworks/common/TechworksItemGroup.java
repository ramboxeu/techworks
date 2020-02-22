package io.github.ramboxeu.techworks.common;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.block.TechworksBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class TechworksItemGroup extends ItemGroup {
    public TechworksItemGroup() {
        super(Techworks.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(TechworksBlocks.boiler);
    }
}
