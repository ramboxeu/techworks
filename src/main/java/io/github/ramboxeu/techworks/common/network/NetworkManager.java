package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class NetworkManager {
    public static Identifier CONTAINER_DATA_SYNC;

    private static Identifier registerS2CPacket(String name, PacketConsumer consumer) {
        Identifier identifier = new Identifier(Techworks.MOD_ID, name);
        ClientSidePacketRegistry.INSTANCE.register(identifier, consumer);

        return identifier;
    }

    public static void sendToPlayer(PlayerEntity entity, Identifier identifier, Consumer<PacketByteBuf> writer) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        writer.accept(data);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(entity, identifier, data);
    }

    public static void registerAllS2C() {
        CONTAINER_DATA_SYNC = registerS2CPacket("container_data_sync", (packetContext, packetByteBuf) -> {
            ScreenHandler container = packetContext.getPlayer().currentScreenHandler;
            int syncId = packetByteBuf.readUnsignedByte();
            int dataId = packetByteBuf.readShort();
            Identifier eventId = packetByteBuf.readIdentifier();
            CompoundTag tag = packetByteBuf.readCompoundTag();

            packetContext.getTaskQueue().execute(() -> {
                Techworks.LOG.info("Received container data sync: {} {} {}", syncId, dataId, tag);

                if (container.syncId == syncId && container instanceof AbstractMachineContainer<?>) {
                    ((AbstractMachineContainer<?>) container).syncData(dataId, eventId, tag);
                }
            });
        });
    }
}
