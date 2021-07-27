package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

public abstract class BaseTagsProvider<T> extends TagsProvider<T> {
    protected final Type type;

    public BaseTagsProvider(DataGenerator generatorIn, Registry<T> registry, ExistingFileHelper helper, Type type) {
        super(generatorIn, registry, Techworks.MOD_ID, helper, type.folder);
        this.type = type;
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/" + folder + "/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return type.name + " Tags: techworks";
    }

    protected enum Type {
        ITEMS("Item", "items"),
        BLOCKS("Block", "blocks"),
        FLUIDS("Fluid", "fluids");

        private final String name;
        private final String folder;

        Type(String name, String folder) {
            this.name = name;
            this.folder = folder;
        }
    }
}
