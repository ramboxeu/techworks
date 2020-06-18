package io.github.ramboxeu.techworks.common.api.sync;

import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ContainerManager {
    private static final Map<ServerPlayerEntity, Optional<AbstractMachineContainer<?>>> containers = new HashMap<>();

    public static void onContainerOpen(AbstractMachineContainer<?> container, ServerPlayerEntity player) {
        if (containers.containsKey(player)) {
            containers.get(player).ifPresent(AbstractMachineContainer::unsubscribeEmitters);
        }

        containers.put(player, Optional.of(container));
        container.subscribeEmitters();
    }

    public static void onContainerClose(AbstractMachineContainer<?> container, ServerPlayerEntity player) {
        if (containers.containsKey(player)) {
            containers.get(player).ifPresent(AbstractMachineContainer::unsubscribeEmitters);
        }

        containers.put(player, Optional.empty());
    }
}
