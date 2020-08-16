package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TechworksPacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Techworks.MOD_ID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, CableSyncShapePacket.class, CableSyncShapePacket::encode, CableSyncShapePacket::decode, CableSyncShapePacket.Handler::handle);
        CHANNEL.registerMessage(id++, CableRequestSyncShapePacket.class, CableRequestSyncShapePacket::encode, CableRequestSyncShapePacket::decode, CableRequestSyncShapePacket.Handler::handle);
        CHANNEL.registerMessage(id++, DebugRequestPacket.class, DebugRequestPacket::encode, DebugRequestPacket::decode, DebugRequestPacket.Handler::handle);
        CHANNEL.registerMessage(id++, DebugResponsePacket.class, DebugResponsePacket::encode, DebugResponsePacket::decode, DebugResponsePacket.Handler::handle);
        CHANNEL.registerMessage(id++, SObjectUpdatePacket.class, SObjectUpdatePacket::encode, SObjectUpdatePacket::decode, SObjectUpdatePacket::handle);
        CHANNEL.registerMessage(id++, SMachinePortSyncPacket.class, SMachinePortSyncPacket::encode, SMachinePortSyncPacket::decode, SMachinePortSyncPacket::handle);
        CHANNEL.registerMessage(id++, CBlueprintCraftPacket.class, CBlueprintCraftPacket::encode, CBlueprintCraftPacket::decode, CBlueprintCraftPacket::handle);
    }

    public static void sendObjectUpdatePacket(SObjectUpdatePacket packet, ServerPlayerEntity entity) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> entity), packet);
    }

    public static void sendMachinePortUpdatePacket(BlockPos pos, int index, MachinePort port, Chunk chunk) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SMachinePortSyncPacket(
                pos,
                index,
                port.getType().ordinal(),
                port.getMode().ordinal()
        ));
    }

    public static void sendCableSyncPacket(Chunk chunk, CableSyncShapePacket packet) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);
    }

    public static void sendBlueprintCraftPacket(CBlueprintCraftPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendRequestCableSyncPacket(CableRequestSyncShapePacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sentDebugRequestPacket(DebugRequestPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendDebugResponsePacket(ServerPlayerEntity playerEntity, DebugResponsePacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> playerEntity), packet);
    }
}
