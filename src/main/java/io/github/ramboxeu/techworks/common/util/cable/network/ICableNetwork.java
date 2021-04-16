package io.github.ramboxeu.techworks.common.util.cable.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.UUID;

public interface ICableNetwork extends INBTSerializable<CompoundNBT> {
    NetworkType getType();
    UUID getId();

    Collection<IEndpointNode> getAllEndpoints();

    void tick();

    @FunctionalInterface
    interface Factory {
        ICableNetwork create(UUID id);
    }
}
