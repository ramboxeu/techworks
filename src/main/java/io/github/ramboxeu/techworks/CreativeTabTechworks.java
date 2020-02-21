package io.github.ramboxeu.techworks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CreativeTabTechworks extends CreativeTabs {
    public CreativeTabTechworks() {
        super("techworks");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Blocks.BEACON);
    }
}
