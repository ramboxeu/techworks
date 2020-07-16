package io.github.ramboxeu.techworks.common.capability;

import io.github.ramboxeu.techworks.client.container.IExtendedContainerListener;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface IExtendedListenerProvider {
    public IExtendedContainerListener create(ServerPlayerEntity entity);
}
