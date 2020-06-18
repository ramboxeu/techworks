package io.github.ramboxeu.techworks.common.mixin;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.sync.ContainerManager;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.container.Container;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(ContainerProviderImpl.class)
public class ContainerProviderImplMixin {
    @Inject(
        method = "openContainer(Lnet/minecraft/util/Identifier;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void openContainer(Identifier identifier, ServerPlayerEntity player, Consumer<PacketByteBuf> writer, CallbackInfo callbackInfo, int syncId, PacketByteBuf buf, PacketByteBuf clonedBuf, Container container) {
        Techworks.LOG.info("Opening container: {} {}", container, player);
        if (container instanceof AbstractMachineContainer<?>) {
            ContainerManager.onContainerOpen((AbstractMachineContainer<?>) container, player);
        }
    }
}
