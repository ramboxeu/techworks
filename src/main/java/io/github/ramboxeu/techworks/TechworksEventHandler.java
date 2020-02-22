package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.block.TechworksBlocks;
import io.github.ramboxeu.techworks.common.gas.Gas;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import static io.github.ramboxeu.techworks.Techworks.LOGGER;

@Mod.EventBusSubscriber(modid = Techworks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TechworksEventHandler {
    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        IForgeRegistry<Gas> gasRegistry = new RegistryBuilder().setName(new ResourceLocation("techworks", "gas")).setType(Gas.class).create();
    }

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

    @SubscribeEvent
    public static void registerGases(RegistryEvent.Register<Gas> event) {
        LOGGER.info("Registering gases");
    }
}
