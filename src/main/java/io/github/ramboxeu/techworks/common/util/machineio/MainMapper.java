package io.github.ramboxeu.techworks.common.util.machineio;

import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface MainMapper {
    /**
     *
     * @param cap
     * @param side
     * @return capability holder, it's assumed to be valid and thus casted with .cast(), {@code null} makes other stuff run
     */
    @Nullable
    LazyOptional<?> getCapability(@Nonnull Capability<?> cap, Side side);
}
