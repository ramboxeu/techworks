package io.github.ramboxeu.techworks.client;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.DataConstants;
import io.github.ramboxeu.techworks.common.registry.ItemRegistryObject;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TechworksItemModelProvider extends ItemModelProvider {
    public TechworksItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Techworks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (ItemRegistryObject<?> itemGenerated : DataConstants.Items.ITEM_GENERATED) {
            itemGenerated(itemGenerated.getId().getPath());
        }

        for (ItemRegistryObject<?> blueprint : DataConstants.Items.BLUEPRINTS) {
            singleTexture(blueprint.getId().getPath(), DataConstants.Textures.ITEM_GENERATED, "layer0", modLoc("item/blueprint"));
        }

        singleTexture("wrench", DataConstants.Textures.ITEM_GENERATED, "layer0", modLoc("item/wrench")).transforms()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT).rotation(0, 90, 25).scale(1, 1, 1).end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(0, -90, -25).scale(1, 1, 1).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT).rotation(0, 90, 55).scale(.85f, .85f, .85f).end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(0, -90, -55).scale(.85f, .85f, .85f).end()
                .end();

        itemGenerated("empty_blueprint");
    }

    private void itemGenerated(String name) {
        singleTexture(name, DataConstants.Textures.ITEM_GENERATED, "layer0", modLoc("item/" + name));
    }
}
