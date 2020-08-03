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
        func_240522_a_(TechworksFluidTags.STEAM).func_240534_a_(TechworksFluids.STEAM.getRight().get(), TechworksFluids.STEAM.getLeft().get());
    }
}
