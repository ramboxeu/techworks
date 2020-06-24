package io.github.ramboxeu.techworks.common.mixin;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.sync.ContainerManager;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.OptionalInt;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow
    private int screenHandlerSyncId;

    @Inject(method = "closeCurrentScreen()V", at = @At("HEAD"))
    private void onCurrentScreenClose(CallbackInfo callbackInfo) {
        ScreenHandler container = ((PlayerEntity) (Object) this).currentScreenHandler;
        Techworks.LOG.info("Closing container: {}", container);
        if (container instanceof AbstractMachineContainer) {
            ContainerManager.onContainerClose((AbstractMachineContainer<?>) container, ((ServerPlayerEntity) (Object) this));
        }
    }

    @Inject(
            method = "openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt",
            at = @At(value = "RETURN", ordinal = 2),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onHandledScreenOpen(NamedScreenHandlerFactory factory, CallbackInfoReturnable<OptionalInt> info, ScreenHandler screenHandler) {
        ScreenHandler container = ((PlayerEntity) (Object) this).currentScreenHandler;
        Techworks.LOG.info("Opening container: {}", container);
        if (container instanceof AbstractMachineContainer) {
            ContainerManager.onContainerOpen((AbstractMachineContainer<?>) container, ((ServerPlayerEntity) (Object) this));
        }
    }
}
