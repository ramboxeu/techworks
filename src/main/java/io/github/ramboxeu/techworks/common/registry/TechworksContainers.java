package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.container.machine.MachineComponentsContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class TechworksContainers {
    public static ScreenHandlerType<BoilerContainer> BOILER;
    public static ScreenHandlerType<MachineComponentsContainer> MACHINE_COMPONENTS;

    private static <T extends ScreenHandler, U extends BlockEntity> ScreenHandlerType<T> registerBlockEntityContainer(String name, BlockEntityContainerFactory<T> factory) {
        return registerExtended(name, (syncId, playerInventory, packetByteBuf) -> {
            BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(packetByteBuf.readBlockPos());
            int dataSize = packetByteBuf.readInt();

            return factory.create(syncId, playerInventory, blockEntity, dataSize);
        });
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> registerExtended(String name, ScreenHandlerRegistry.ExtendedClientHandlerFactory<T> factory) {
        return ScreenHandlerRegistry.registerExtended(new Identifier(Techworks.MOD_ID, name), factory);
    }

    public static void registerAll() {
        BOILER = registerBlockEntityContainer("boiler", (syncId, playerInventory, blockEntity, dataSize) ->
                new BoilerContainer(syncId, playerInventory, (BoilerBlockEntity) blockEntity, dataSize));
        MACHINE_COMPONENTS = registerExtended("machine_components", MachineComponentsContainer::factory);
    }

    @FunctionalInterface
    interface BlockEntityContainerFactory<T extends ScreenHandler> {
        T create(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, int dataSize);
    }
}
