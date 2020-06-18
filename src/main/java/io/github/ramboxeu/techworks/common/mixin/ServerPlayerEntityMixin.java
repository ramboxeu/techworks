package io.github.ramboxeu.techworks.common.mixin;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.sync.ContainerManager;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow
    private int containerSyncId;

    @Inject(method = "closeCurrentScreen()V", at = @At("HEAD"))
    private void onContainerClose(CallbackInfo callbackInfo) {
        Techworks.LOG.info("Closing container: {} {}", ((PlayerEntity) (Object) this), containerSyncId);
        Container container = ((PlayerEntity) (Object) this).container;
        if (container instanceof AbstractMachineContainer<?>) {
            ContainerManager.onContainerClose((AbstractMachineContainer<?>) container, ((ServerPlayerEntity) (Object) this));
        }
    }
}
