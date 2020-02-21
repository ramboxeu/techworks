package io.github.ramboxeu.techworks;

import io.github.ramboxeu.techworks.common.block.TechworksBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Techworks.LOGGER.info("Proxy preInit");
    }

    public void init(FMLInitializationEvent event) {
        Techworks.LOGGER.info("Proxy init");
    }

    public void postInit(FMLPostInitializationEvent event) {
        Techworks.LOGGER.info("Proxy postInit");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Techworks.LOGGER.info("Registering block");
        event.getRegistry().register(TechworksBlocks.boiler);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Techworks.LOGGER.info("Registering items");
        event.getRegistry().register(new ItemBlock(TechworksBlocks.boiler).setRegistryName(TechworksBlocks.boiler.getRegistryName()));
    }
}
