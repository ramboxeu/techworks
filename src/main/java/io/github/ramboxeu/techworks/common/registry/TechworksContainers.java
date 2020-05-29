package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;

public class TechworksContainers {
    public static Identifier BOILER;

    private static Identifier registerMachineContainer(String name, MachineContainerFactory factory) {
        return register(name, (syncid, identifier, playerEntity, packetByteBuf) -> {
            BlockEntity blockEntity = playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());

            return factory.create(syncid, playerEntity.inventory, blockEntity);
        });
    }

    private static Identifier register(String name, ContainerFactory<Container> factory) {
        Identifier identifier = new Identifier(Techworks.MOD_ID, name);
        ContainerProviderRegistry.INSTANCE.registerFactory(identifier, factory);
        return identifier;
    }

    public static void registerAll() {
        BOILER = registerMachineContainer("boiler", (syncid, inventory, blockEntity) -> new BoilerContainer(syncid, inventory, (BoilerBlockEntity) blockEntity));
    }

    @FunctionalInterface
    interface MachineContainerFactory {
        Container create(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity);
    }
}
