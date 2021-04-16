package io.github.ramboxeu.techworks.client;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.DataConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CableModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    public CableModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(DataConstants.Misc.CABLE_LOADER, parent, existingFileHelper);
    }

    public CableModelBuilder<T> particle(String name) {
        parent.texture("particle", modLoc(name));
        return this;
    }

    public CableModelBuilder<T> base(String name) {
        parent.texture("base", modLoc(name));
        return this;
    }

    public CableModelBuilder<T> connector(String name) {
        parent.texture("connector", modLoc(name));
        return this;
    }

    private ResourceLocation modLoc(String name) {
        return new ResourceLocation(Techworks.MOD_ID, name);
    }
}
