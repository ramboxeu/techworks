package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.Utils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;

public class TechworksItemTagProvider extends TagsProvider<Item> {
    public TechworksItemTagProvider(DataGenerator generator) {
        super(generator, Registry.ITEM, Techworks.MOD_ID);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(TechworksItemTags.BLUEPRINTS).add(Utils.unpackItemObjectsArray(DataConstants.Items.BLUEPRINTS));
        getOrCreateBuilder(TechworksItemTags.MACHINES).add(Utils.unpackBlockItemArray(DataConstants.Blocks.MACHINES));
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Item Tags : techworks";
    }
}
