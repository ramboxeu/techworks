package io.github.ramboxeu.techworks.common;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.TechworksBlockStateProvider;
import io.github.ramboxeu.techworks.client.TechworksItemModelProvider;
import io.github.ramboxeu.techworks.common.loot.TechworksLootProvider;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTagProvider;
import io.github.ramboxeu.techworks.common.tag.TechworksItemTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Techworks.MOD_ID, bus = Bus.MOD)
public class TechworksDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)  {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(new TechworksBlockStateProvider(generator, existingFileHelper));
            generator.addProvider(new TechworksItemModelProvider(generator, existingFileHelper));
        }

        if (event.includeServer()) {
            generator.addProvider(new TechworksLootProvider(generator));
            generator.addProvider(new TechworksFluidTagProvider(generator, existingFileHelper));
            generator.addProvider(new TechworksItemTagProvider(generator, existingFileHelper));
        }
    }
}