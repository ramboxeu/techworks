package io.github.ramboxeu.techworks.common.tag;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class TechworksItemTags {
    public static final ITag.INamedTag<Item> BLUEPRINTS = tag("blueprints");

    public static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.makeWrapperTag("techworks:" + name);
    }
}
