package io.github.ramboxeu.techworks.common.capability;

import io.github.ramboxeu.techworks.client.container.IExtendedContainerListener;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface IExtendedListenerProvider {
    IExtendedContainerListener create(ServerPlayerEntity entity);
}
