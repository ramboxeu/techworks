package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.block.TechworksBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.ramboxeu.techworks.Techworks.LOGGER;

@Mod.EventBusSubscriber(modid = Techworks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TechworksEventHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        LOGGER.info("Registering blocks");
        TechworksBlocks.registerBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        LOGGER.info("Registering items");
        TechworksBlocks.registerItemBlocks(event.getRegistry());
    }
}
