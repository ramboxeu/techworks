package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.capability.ExtendedListenerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TechworksEvents {

    @SubscribeEvent
    static void attachCapabilitiesToEntities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation(Techworks.MOD_ID, "extended_listener_provider"), new ExtendedListenerProvider());
        }
    }
}
