package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ContainerDeferredRegister {
    private final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Techworks.MOD_ID);

    @SuppressWarnings("unchecked")
    public  <TILE extends BaseMachineTile, CONTAINER extends BaseMachineContainer<TILE>> ContainerRegistryObject<CONTAINER> registerMachineContainer(String name, IMachineContainerFactory<TILE, CONTAINER> factory) {
        return register(name, (id, inv, buf) -> {
            BlockPos pos = buf.readBlockPos();
            TileEntity te = inv.player.world.getTileEntity(pos);
            if (te instanceof BaseMachineTile) {
                return factory.create(id, inv, (TILE) te);
            } else {
                throw new IllegalStateException("Expected BaseMachineTile on " + buf + " but it was not found!");
            }
        });
    }

    public <CONTAINER extends Container> ContainerRegistryObject<CONTAINER> register(String name, IContainerFactory<CONTAINER> factory) {
        return register(name, () -> IForgeContainerType.create(factory));
    }

    public <CONTAINER extends Container> ContainerRegistryObject<CONTAINER> register(String name, Supplier<ContainerType<CONTAINER>> containerSupplier) {
        return new ContainerRegistryObject<>(CONTAINERS.register(name, containerSupplier));
    }

    public void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }

    public interface IMachineContainerFactory<TILE extends BaseMachineTile, CONTAINER extends BaseMachineContainer<TILE>> {
        CONTAINER create(int id, PlayerInventory inventory, TILE tile);
    }
}
