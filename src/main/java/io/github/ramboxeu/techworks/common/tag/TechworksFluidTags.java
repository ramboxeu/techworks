package io.github.ramboxeu.techworks.common.tag;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

public class TechworksFluidTags {
    public static final ITag.INamedTag<Fluid> STEAM = forgeTag("steam");

    public static ITag.INamedTag<Fluid> forgeTag(String name) {
        return FluidTags.makeWrapperTag("forge:" + name);
    }
}
