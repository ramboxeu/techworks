package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.api.wrench.IWrench;
import io.github.ramboxeu.techworks.common.command.CablesCommand;
import io.github.ramboxeu.techworks.common.command.CapabilitiesCommand;
import io.github.ramboxeu.techworks.common.component.Component;
import io.github.ramboxeu.techworks.common.component.ComponentManager;
import io.github.ramboxeu.techworks.common.item.WrenchItem;
import io.github.ramboxeu.techworks.common.item.WrenchItem.Result;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.util.cable.network.CableNetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

// Events must be public, otherwise HotSwap won't work
public class TechworksEvents {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.START) {
            CableNetworkManager.get(event.world).tickNetworks();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEntity player = event.getPlayer();

        double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
        RayTraceResult traceResult = player.pick(reach, 1.0f, false);

        if (traceResult.getType() != RayTraceResult.Type.MISS) {
            // Hand is always MAIN_HAND
            ItemStack stack = player.getHeldItem(event.getHand());
            Item item = stack.getItem();
            Result result = null;
            BlockPos pos = event.getPos();
            Vector3d hitVec = traceResult.getHitVec().subtract(pos.getX(), pos.getY(), pos.getZ());

            if (item == TechworksItems.WRENCH.get()) {
                result = ((WrenchItem) stack.getItem()).onLeftClickBlock(player, event.getWorld(), pos, event.getFace(), hitVec, stack);
            } else if (item instanceof IWrench) {
                result = WrenchItem.useExternalWrench(((IWrench) item).leftClick(player), event.getWorld(), pos, event.getFace(), hitVec, stack);
            }

            if (result != null) {
                event.setCanceled(result.cancelsEvent());
                event.setCancellationResult(result.getResultType());
                event.setUseBlock(result.getBlockResult());
                event.setUseItem(result.getItemResult());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();

        double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
        RayTraceResult traceResult = player.pick(reach, 1.0f, false);

        if (traceResult.getType() != RayTraceResult.Type.MISS) {
            ItemStack stack = player.getHeldItem(event.getHand());
            Item item = stack.getItem();
            Result result = null;
            BlockPos pos = event.getPos();
            Vector3d hitVec = traceResult.getHitVec().subtract(pos.getX(), pos.getY(), pos.getZ());

            if (item == TechworksItems.WRENCH.get()) {
                result = ((WrenchItem) item).onRightClick(player, event.getWorld(), pos, event.getFace(), hitVec, stack);
            } else if (item instanceof IWrench) {
                result = WrenchItem.useExternalWrench(((IWrench) item).rightClick(player), event.getWorld(), pos, event.getFace(), hitVec, stack);
            }

            if (result != null) {
                event.setCanceled(result.cancelsEvent());
                event.setCancellationResult(result.getResultType());
                event.setUseBlock(result.getBlockResult());
                event.setUseItem(result.getItemResult());
            }
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        CapabilitiesCommand.register(event.getDispatcher());
        CablesCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(ComponentManager.createListener());
    }

    private static final Style COMPONENT_NAME = Style.EMPTY.setColor(Color.fromInt(0x555555));

    @SubscribeEvent
    public static void onGetItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();

        if (item.isIn(ComponentManager.getInstance().getItemComponentTag())) {
            Component component = ComponentManager.getInstance().getComponent(item);
            String name = component.getType().getName().getString();

            List<ITextComponent> toolTip = event.getToolTip();
            toolTip.add(new StringTextComponent(name).setStyle(COMPONENT_NAME));
        }
    }
}
