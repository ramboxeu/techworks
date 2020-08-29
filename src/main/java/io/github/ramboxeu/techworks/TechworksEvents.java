package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.api.wrench.IWrench;
import io.github.ramboxeu.techworks.common.capability.ExtendedListenerProvider;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import io.github.ramboxeu.techworks.common.item.WrenchItem.Result;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        Item item = stack.getItem();
        Result result = null;

        if (item == TechworksItems.WRENCH.getItem()) {
            result = ((WrenchItem) stack.getItem()).onLeftClickBlock(player, event.getWorld(), event.getPos(), event.getFace(), stack);
        } else if (item instanceof IWrench) {
            result = WrenchItem.useExternalWrench(((IWrench) item).leftClick(player), event.getWorld(), event.getPos(), event.getFace(), stack);
        }

        if (result != null) {
            event.setCanceled(result.cancelsEvent());
            event.setCancellationResult(result.getResultType());
            event.setUseBlock(result.getBlockResult());
            event.setUseItem(result.getItemResult());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();

        ItemStack stack = player.getHeldItem(event.getHand());
        Item item = stack.getItem();
        Result result = null;

        if (item == TechworksItems.WRENCH.getItem()) {
            result = ((WrenchItem) item).onRightClick(player, event.getWorld(), event.getPos(), event.getFace(), stack);
        } else if (item instanceof IWrench) {
            result = WrenchItem.useExternalWrench(((IWrench) item).rightClick(player), event.getWorld(), event.getPos(), event.getFace(), stack);
        }

        if (result != null) {
            event.setCanceled(result.cancelsEvent());
            event.setCancellationResult(result.getResultType());
            event.setUseBlock(result.getBlockResult());
            event.setUseItem(result.getItemResult());
        }
    }
}
