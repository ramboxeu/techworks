package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.capability.ExtendedListenerProvider;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TechworksEvents {

    @SubscribeEvent
    static void attachCapabilitiesToEntities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation(Techworks.MOD_ID, "extended_listener_provider"), new ExtendedListenerProvider());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEntity player = event.getPlayer();

        // Hand is always MAIN_HAND
        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.getItem() == TechworksItems.WRENCH.get()) {
            event.setCanceled(((WrenchItem) stack.getItem()).onLeftClickBlock(player, event.getWorld(), event.getPos(), event.getFace(), stack));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();

        ItemStack stack = player.getHeldItem(event.getHand());

        if (stack.getItem() == TechworksItems.WRENCH.get()) {
            Techworks.LOGGER.debug(player.getLook(1));
            Techworks.LOGGER.debug(player.getLookVec());

            boolean success = ((WrenchItem) stack.getItem()).onRightClick(player, event.getWorld(), event.getPos(), event.getFace(), stack);
            event.setUseItem(Event.Result.DENY);
            // Use this or write some vector magic to figure out hitVector?
            event.setUseBlock(success ? Event.Result.DENY : Event.Result.ALLOW);
            event.setCanceled(success);
            event.setCancellationResult(success ? ActionResultType.FAIL : ActionResultType.CONSUME);
        }
    }
}
