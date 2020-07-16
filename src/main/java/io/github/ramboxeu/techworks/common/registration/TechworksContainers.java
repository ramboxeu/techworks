package io.github.ramboxeu.techworks.common.registration;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.client.container.machine.ComponentsContainer;
import io.github.ramboxeu.techworks.common.component.IComponentsContainerProvider;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
//                CompoundNBT nbt = buf.readCompoundTag();
                ComponentStackHandler components = ComponentStackHandler.empty();
//                components.deserializeNBT(nbt);

                BlockPos pos = buf.readBlockPos();
                TileEntity te = inv.player.world.getTileEntity(pos);

                if (te instanceof IComponentsContainerProvider) {
                    components = ((IComponentsContainerProvider)te).getComponentsStackHandler();
                } else {
                    // Should this crash, or something?
                    Techworks.LOGGER.warn("Expected IComponentsContainerProvider on " + pos + ", but it was not found.");
                }

                return new ComponentsContainer(id, inv, components);
            }));
}
