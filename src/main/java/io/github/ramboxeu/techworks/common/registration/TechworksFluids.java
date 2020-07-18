package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.util.Color;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.fluids.ForgeFlowingFluid.*;

public class TechworksFluids {
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Techworks.MOD_ID);

    public static final Pair<RegistryObject<Source>, RegistryObject<Flowing>> STEAM = register("steam",
            FluidAttributes.builder(new ResourceLocation(Techworks.MOD_ID, "block/water_still"), new ResourceLocation(Techworks.MOD_ID, "block/water_still")).gaseous().color(Color.toRGBA(229, 229, 299, 200)),
            null, Items.AIR);

    private static Pair<RegistryObject<Source>, RegistryObject<Flowing>> register(String name, FluidAttributes.Builder builder, FlowingFluidBlock block, Item bucket) {
        String flowingName = "flowing_" + name;

        RegistryObject<Source> sourceObject = RegistryObject.of(new ResourceLocation(Techworks.MOD_ID, name), ForgeRegistries.FLUIDS);
        RegistryObject<Flowing> flowingObject = RegistryObject.of(new ResourceLocation(Techworks.MOD_ID, flowingName), ForgeRegistries.FLUIDS);
        Properties properties = new Properties(sourceObject, flowingObject, builder);

        properties.block(() -> block);
        properties.bucket(() -> bucket);

        sourceObject = FLUIDS.register(name, () -> new Source(properties));
        flowingObject = FLUIDS.register(flowingName, () -> new Flowing(properties));

        return Pair.of(sourceObject, flowingObject);
    }

    public static void addToEventBus() {
        FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
