package io.github.ramboxeu.techworks.common.registry;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.blockentity.machine.BoilerBlockEntity;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.container.machine.MachineComponentsContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;

// FIXME: Again complete rewrite
public class TechworksContainers {
    public static Identifier BOILER;
    public static Identifier MACHINE_COMPONENTS;

//    private static <T extends BlockEntity> Identifier registerMachineContainer(String name, MachineContainerFactory factory) {
//        return register(name, (syncid, identifier, playerEntity, packetByteBuf) -> {
//            BlockEntity blockEntity = playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());
//
////            if (playerEntity instanceof ServerPlayerEntity) {
////                //Techworks.LOG.info("Var: {} | Player: {} | Mixin: {} | Calc: {}", syncid, playerEntity.container.syncId,
//////                ((ServerPlayerEntityAccessor) playerEntity).getContainerSyncId(), (playerEntity.container.syncId + 1) % 100);
////            }
//
//            int dataSize = packetByteBuf.readInt();
////            Techworks.LOG.info(dataSize);
//
//            return factory.create(syncid, playerEntity.inventory, blockEntity, dataSize);
//        });
//    }
//
//    private static Identifier register(String name, ContainerFactory<Container> factory) {
//        Identifier identifier = new Identifier(Techworks.MOD_ID, name);
//        ScreenHandlerRegistry.registerExtended()
//        return identifier;
//    }

    public static void registerAll() {
//        BOILER = registerMachineContainer("boiler", (syncId, playerInventory, blockEntity, dataSize) ->
//                new BoilerContainer(syncId, playerInventory, (BoilerBlockEntity) blockEntity, dataSize));
//        MACHINE_COMPONENTS = register("machine_components", MachineComponentsContainer::factory);
    }

//    @FunctionalInterface
//    interface MachineContainerFactory {
//        Container create(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, int dataSize);
//    }
}
