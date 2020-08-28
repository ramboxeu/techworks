package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.Utils;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;

public class TechworksItemTagProvider extends TagsProvider<Item> {
    public TechworksItemTagProvider(DataGenerator generator) {
        super(generator, Registry.ITEM, Techworks.MOD_ID);
    }

    @Override
    protected void registerTags() {
        func_240522_a_(TechworksItemTags.BLUEPRINTS).func_240534_a_(Utils.unpackItemObjectsArray(DataConstants.Items.BLUEPRINTS));
        func_240522_a_(TechworksItemTags.MACHINES).func_240534_a_(Utils.unpackBlockItemArray(DataConstants.Blocks.MACHINES));
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
