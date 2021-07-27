package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registry.BlockRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ITag;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class TechworksBlockTagsProvider extends BaseTagsProvider<Block> {

    @SuppressWarnings("deprecation")
    public TechworksBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper helper) {
        super(generatorIn, Registry.BLOCK, helper, Type.BLOCKS);
    }

    @Override
    protected void registerTags() {
        makeTag(TechworksBlockTags.LITHIUM_ORES, TechworksBlocks.LITHIUM_ORE);
        makeTag(TechworksBlockTags.COPPER_ORES, TechworksBlocks.COPPER_ORE);
        makeTag(Tags.Blocks.ORES, TechworksBlocks.COPPER_ORE, TechworksBlocks.LITHIUM_ORE);
    }

    private Builder<Block> makeTag(ITag.INamedTag<Block> tag, BlockRegistryObject<?, ?>... blocks) {
        Builder<Block> builder = getOrCreateBuilder(tag);
        Arrays.stream(blocks).forEach(obj -> builder.add(obj.get()));
        return builder;
    }
}
