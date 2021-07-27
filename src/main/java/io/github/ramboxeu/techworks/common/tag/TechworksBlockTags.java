package io.github.ramboxeu.techworks.common.tag;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class TechworksBlockTags {
    public static final ITag.INamedTag<Block> MACHINES = tag("machines");
    public static final ITag.INamedTag<Block> LITHIUM_ORES = forgeTag("ores/lithium");
    public static final ITag.INamedTag<Block> COPPER_ORES = forgeTag("ores/copper");

    private static ITag.INamedTag<Block> tag(String name) {
        return BlockTags.makeWrapperTag("techworks:" + name);
    }

    private static ITag.INamedTag<Block> forgeTag(String name) {
        return BlockTags.makeWrapperTag("forge:" + name);
    }
}
