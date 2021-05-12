package io.github.ramboxeu.techworks.client.container.sync;

import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.function.Consumer;

public class PlayerListenerWrapper implements IExtendedContainerListener {

    private final ServerPlayerEntity player;

    public PlayerListenerWrapper(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    public void sendObjectUpdate(int holderId, ObjectHolder<?> holder) {
        TechworksPacketHandler.syncObjectHolder(player, holderId, holder);
    }

    public static void wrap(PlayerEntity player, Consumer<PlayerListenerWrapper> consumer) {
        if (player instanceof ServerPlayerEntity) {
            consumer.accept(new PlayerListenerWrapper((ServerPlayerEntity) player));
        }
    }
}
