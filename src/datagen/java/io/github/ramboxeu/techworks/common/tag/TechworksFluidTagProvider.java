package io.github.ramboxeu.techworks.common.tag;

import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TechworksFluidTagProvider extends BaseTagsProvider<Fluid> {

    @SuppressWarnings("deprecation")
    public TechworksFluidTagProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Registry.FLUID, helper, Type.FLUIDS);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(TechworksFluidTags.STEAM).add(TechworksFluids.STEAM.get());
    }
}
