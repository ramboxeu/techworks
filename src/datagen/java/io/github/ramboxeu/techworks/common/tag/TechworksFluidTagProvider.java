package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TechworksFluidTagProvider extends FluidTagsProvider {
    public TechworksFluidTagProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Techworks.MOD_ID, helper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(TechworksFluidTags.STEAM).add(TechworksFluids.STEAM.get());
    }
}
