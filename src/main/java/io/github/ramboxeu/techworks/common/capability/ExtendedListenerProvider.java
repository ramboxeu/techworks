package io.github.ramboxeu.techworks.common.capability;

import io.github.ramboxeu.techworks.client.container.IExtendedContainerListener;
import io.github.ramboxeu.techworks.common.network.SObjectUpdatePacket;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExtendedListenerProvider implements ICapabilityProvider, IExtendedListenerProvider {

    private final LazyOptional<IExtendedListenerProvider> holder = LazyOptional.of(() -> this);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityExtendedListenerProvider.EXTENDED_LISTENER_PROVIDER) {
            return CapabilityExtendedListenerProvider.EXTENDED_LISTENER_PROVIDER.orEmpty(cap, holder);
        }

        return LazyOptional.empty();
    }

    @Override
    public IExtendedContainerListener create(ServerPlayerEntity entity) {
        return (container, index, tag) -> TechworksPacketHandler.sendObjectUpdatePacket(new SObjectUpdatePacket(container.windowId, index, tag), entity);
    }
}
