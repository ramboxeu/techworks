package io.github.ramboxeu.techworks.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class WrenchItem extends Item {
    public WrenchItem() {
        super(new Item.Settings().maxCount(1).group(ItemGroup.REDSTONE));
    }
}
