package io.github.ramboxeu.techworks.common.tag;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class TechworksBlockTags {
    public static final ITag.INamedTag<Block> MACHINES = tag("machines");

    public static ITag.INamedTag<Block> tag(String name) {
        return BlockTags.makeWrapperTag("techworks:" + name);
    }
}
