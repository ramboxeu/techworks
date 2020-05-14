package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TechworkPacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Techworks.MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, CableSyncShapePacket.class, CableSyncShapePacket::encode, CableSyncShapePacket::decode, CableSyncShapePacket.Handler::handle);
        CHANNEL.registerMessage(id++, CableRequestSyncShapePacket.class, CableRequestSyncShapePacket::encode, CableRequestSyncShapePacket::decode, CableRequestSyncShapePacket.Handler::handle);
    }

    public static void sendCableSyncPacket(Chunk chunk, CableSyncShapePacket packet) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);
    }

    public static void sendRequestCableSyncPacket(CableRequestSyncShapePacket packet) {
        CHANNEL.sendToServer(packet);
    }
}
