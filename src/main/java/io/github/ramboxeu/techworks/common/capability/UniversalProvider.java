package io.github.ramboxeu.techworks.common.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UniversalProvider<CAP> implements ICapabilityProvider {
    private final Capability<CAP> capability;
    private final LazyOptional<CAP> holder;

    public UniversalProvider(Capability<CAP> cap, CAP instance) {
        this(cap, LazyOptional.of(() -> instance));
    }

    public UniversalProvider(Capability<CAP> cap, LazyOptional<CAP> holder) {
        this.capability = cap;
        this.holder = holder;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, holder);
    }
}
