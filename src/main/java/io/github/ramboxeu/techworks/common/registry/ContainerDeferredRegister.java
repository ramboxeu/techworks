package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ContainerDeferredRegister {
    private final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Techworks.MOD_ID);

    @SuppressWarnings("unchecked")
    public  <T extends BaseMachineTile, U extends BaseMachineContainer<T>> ContainerRegistryObject<U> registerMachineContainer(String name, IMachineContainerFactory<T, U> factory) {
        return register(name, (id, inv, buf) -> {
            BlockPos pos = buf.readBlockPos();
            TileEntity te = inv.player.world.getTileEntity(pos);
            if (te instanceof BaseMachineTile) {
                return factory.create(id, inv, (T) te, ((BaseMachineTile) te).getMachineIO().createDataMap());
            } else {
                throw new IllegalStateException("Expected BaseMachineTile on " + pos + " but it was not found!");
            }
        });
    }

    @SuppressWarnings("unchecked")
    public  <T extends TileEntity, U extends Container> ContainerRegistryObject<U> registerTileContainer(String name, ITileContainerFactory<T, U> factory) {
        return register(name, (id, inv, buf) -> {
            BlockPos pos = buf.readBlockPos();
            TileEntity te = inv.player.world.getTileEntity(pos);
            if (te != null) {
                return factory.create(id, inv, (T) te);
            } else {
                throw new IllegalStateException("Expected TileEntity on " + pos + " but it was not found!");
            }
        });
    }

    public <T extends Container> ContainerRegistryObject<T> register(String name, IContainerFactory<T> factory) {
        return register(name, () -> IForgeContainerType.create(factory));
    }

    public <T extends Container> ContainerRegistryObject<T> register(String name, Supplier<ContainerType<T>> containerSupplier) {
        return new ContainerRegistryObject<>(CONTAINERS.register(name, containerSupplier));
    }

    public void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }

    public interface IMachineContainerFactory<T extends BaseMachineTile, U extends BaseMachineContainer<T>> {
        U create(int id, PlayerInventory inventory, T tile, Map<Side, List<HandlerConfig>> configMap);
    }

    public interface ITileContainerFactory<T extends TileEntity, U extends Container> {
        U create(int id, PlayerInventory inventory, T tile);
    }
}
