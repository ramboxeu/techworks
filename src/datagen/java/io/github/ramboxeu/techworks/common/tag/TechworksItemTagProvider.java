package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.Utils;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.registry.IItemSupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class TechworksItemTagProvider extends BaseTagsProvider<Item> {

    @SuppressWarnings("deprecation")
    public TechworksItemTagProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Registry.ITEM, helper, Type.ITEMS);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(TechworksItemTags.BLUEPRINTS).add(Utils.unpackItemObjectsArray(DataConstants.Items.BLUEPRINTS));
        getOrCreateBuilder(TechworksItemTags.MACHINES).add(Utils.unpackBlockItemArray(DataConstants.Blocks.MACHINES));

        makeTag(TechworksItemTags.LITHIUM_ORES, TechworksBlocks.LITHIUM_ORE);
        makeTag(TechworksItemTags.COPPER_ORES, TechworksBlocks.COPPER_ORE);
        makeTag(Tags.Items.ORES, TechworksBlocks.LITHIUM_ORE, TechworksBlocks.COPPER_ORE);

        makeTag(TechworksItemTags.LITHIUM_DUSTS, TechworksItems.LITHIUM_DUST);
        makeTag(TechworksItemTags.COPPER_DUSTS, TechworksItems.COPPER_DUST);
        makeTag(TechworksItemTags.IRON_DUSTS, TechworksItems.IRON_DUST);
        makeTag(TechworksItemTags.GOLD_DUSTS, TechworksItems.GOLD_DUST);
        makeTag(TechworksItemTags.COAL_DUSTS, TechworksItems.COAL_DUST);
        makeTag(Tags.Items.DUSTS, TechworksItems.LITHIUM_DUST, TechworksItems.COPPER_DUST, TechworksItems.IRON_DUST, TechworksItems.GOLD_DUST, TechworksItems.COAL_DUST);

        makeTag(TechworksItemTags.LITHIUM_INGOTS, TechworksItems.LITHIUM_INGOT);
        makeTag(TechworksItemTags.COPPER_INGOTS, TechworksItems.COPPER_INGOT);
        makeTag(Tags.Items.INGOTS, TechworksItems.LITHIUM_INGOT, TechworksItems.COPPER_INGOT);

        makeTag(TechworksItemTags.COPPER_PLATES, TechworksItems.COPPER_PLATE);
        makeTag(TechworksItemTags.IRON_PLATES, TechworksItems.IRON_PLATE);
    }

    private Builder<Item> makeTag(ITag.INamedTag<Item> tag, IItemSupplier... items) {
        Builder<Item> builder = getOrCreateBuilder(tag);
        Arrays.stream(items).forEach(obj -> builder.add(obj.getAsItem()));
        return builder;
    }
}
