package io.github.ramboxeu.techworks.common.tag;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class TechworksItemTags {
    public static final ITag.INamedTag<Item> BLUEPRINTS = tag("blueprints");
    public static final ITag.INamedTag<Item> MACHINES = tag("machines");
    public static final ITag.INamedTag<Item> LITHIUM_ORES = forgeTag("ores/lithium");
    public static final ITag.INamedTag<Item> LITHIUM_INGOTS = forgeTag("ingots/lithium");
    public static final ITag.INamedTag<Item> LITHIUM_DUSTS = forgeTag("dusts/lithium");
    public static final ITag.INamedTag<Item> COPPER_ORES = forgeTag("ores/copper");
    public static final ITag.INamedTag<Item> COPPER_INGOTS = forgeTag("ingots/copper");
    public static final ITag.INamedTag<Item> COPPER_DUSTS = forgeTag("dusts/copper");
    public static final ITag.INamedTag<Item> IRON_DUSTS = forgeTag("dusts/iron");
    public static final ITag.INamedTag<Item> GOLD_DUSTS = forgeTag("dusts/gold");
    public static final ITag.INamedTag<Item> STEEL_INGOTS = forgeTag("ingots/steel");

    public static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.makeWrapperTag("techworks:" + name);
    }

    private static ITag.INamedTag<Item> forgeTag(String name) {
        return ItemTags.makeWrapperTag("forge:" + name);
    }
}
