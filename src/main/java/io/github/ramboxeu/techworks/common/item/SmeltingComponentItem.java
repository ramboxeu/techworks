package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SmeltingComponentItem extends Item {

    public SmeltingComponentItem() {
        super(new Properties().group(Techworks.ITEM_GROUP).maxStackSize(1));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.EFFICIENCY;
    }

}
