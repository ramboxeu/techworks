package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;

public class TechworksFluidTagProvider extends FluidTagsProvider {
    public TechworksFluidTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(TechworksFluidTags.STEAM).add(TechworksFluids.STEAM.get());
    }
}
