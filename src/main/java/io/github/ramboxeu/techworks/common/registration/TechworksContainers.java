package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.ComponentsContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TechworksContainers {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Techworks.MOD_ID);

    public static void addToEventBus() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<ContainerType<ComponentsContainer>> COMPONENTS = CONTAINERS.register("components",
            () -> IForgeContainerType.create((id, inv, buf) -> {
                CompoundNBT nbt = buf.readCompoundTag();
                ComponentStackHandler components = new ComponentStackHandler.Builder().empty();
                components.deserializeNBT(nbt);
                return new ComponentsContainer(id, inv, components);
            }));
}
