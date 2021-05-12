package io.github.ramboxeu.techworks.common.network;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.sync.ObjectHolder;
import io.github.ramboxeu.techworks.common.network.dev.DevBlockEnergySyncPacket;
import io.github.ramboxeu.techworks.common.network.dev.DevBlockGasSyncPacket;
import io.github.ramboxeu.techworks.common.network.dev.DevBlockItemSyncPacket;
import io.github.ramboxeu.techworks.common.network.dev.DevBlockLiquidSyncPacket;
import io.github.ramboxeu.techworks.common.tile.DevBlockTile;
import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnections;
import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.machineio.MachinePort;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.EnumSet;
import java.util.List;

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
        CHANNEL.registerMessage(id++, SyncCableConnectionsPacket.class, SyncCableConnectionsPacket::encode, SyncCableConnectionsPacket::decode, SyncCableConnectionsPacket::handle);
        CHANNEL.registerMessage(id++, CableRequestSyncShapePacket.class, CableRequestSyncShapePacket::encode, CableRequestSyncShapePacket::decode, CableRequestSyncShapePacket.Handler::handle);
        CHANNEL.registerMessage(id++, DebugRequestPacket.class, DebugRequestPacket::encode, DebugRequestPacket::decode, DebugRequestPacket.Handler::handle);
        CHANNEL.registerMessage(id++, DebugResponsePacket.class, DebugResponsePacket::encode, DebugResponsePacket::decode, DebugResponsePacket.Handler::handle);
        CHANNEL.registerMessage(id++, SMachinePortSyncPacket.class, SMachinePortSyncPacket::encode, SMachinePortSyncPacket::decode, SMachinePortSyncPacket::handle);
        CHANNEL.registerMessage(id++, CBlueprintCraftPacket.class, CBlueprintCraftPacket::encode, CBlueprintCraftPacket::decode, CBlueprintCraftPacket::handle);
        CHANNEL.registerMessage(id++, SyncHandlerStatus.class, SyncHandlerStatus::encode, SyncHandlerStatus::decode, SyncHandlerStatus::handle);
        CHANNEL.registerMessage(id++, SyncHandlerConfigMode.class, SyncHandlerConfigMode::encode, SyncHandlerConfigMode::decode, SyncHandlerConfigMode::handle);
        CHANNEL.registerMessage(id++, DevBlockLogSyncPacket.class, DevBlockLogSyncPacket::encode, DevBlockLogSyncPacket::decode, DevBlockLogSyncPacket::handle);
        CHANNEL.registerMessage(id++, DevBlockEnergySyncPacket.class, DevBlockEnergySyncPacket::encode, DevBlockEnergySyncPacket::decode, DevBlockEnergySyncPacket::handle);
        CHANNEL.registerMessage(id++, DevBlockLiquidSyncPacket.class, DevBlockLiquidSyncPacket::encode, DevBlockLiquidSyncPacket::decode, DevBlockLiquidSyncPacket::handle);
        CHANNEL.registerMessage(id++, DevBlockGasSyncPacket.class, DevBlockGasSyncPacket::encode, DevBlockGasSyncPacket::decode, DevBlockGasSyncPacket::handle);
        CHANNEL.registerMessage(id++, DevBlockItemSyncPacket.class, DevBlockItemSyncPacket::encode, DevBlockItemSyncPacket::decode, DevBlockItemSyncPacket::handle);
//        CHANNEL.registerMessage(id++, SyncTransportedItemPacket.class, SyncTransportedItemPacket::encode, SyncTransportedItemPacket::decode, SyncTransportedItemPacket::handle);
        CHANNEL.registerMessage(id++, SyncCablePacket.class, SyncCablePacket::encode, SyncCablePacket::decode, SyncCablePacket::handle);
        CHANNEL.registerMessage(id++, ContainerButtonClicked.class, ContainerButtonClicked::encode, ContainerButtonClicked::decode, ContainerButtonClicked::handle);
        CHANNEL.registerMessage(id++, SyncToggleableButtonState.class, SyncToggleableButtonState::encode, SyncToggleableButtonState::decode, SyncToggleableButtonState::handle);
        CHANNEL.registerMessage(id++, SyncObjectHolder.class, SyncObjectHolder::encode, SyncObjectHolder::decode, SyncObjectHolder::handle);
    }

    public static void sendMachinePortUpdatePacket(BlockPos pos, int index, MachinePort port, Chunk chunk) {
        // FIXME: 10/10/2020 
//        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SMachinePortSyncPacket(
//                pos,
//                index,
//                port.getType().ordinal(),
//                port.getMode().ordinal()
//        ));
    }

    public static void syncCableConnections(Chunk chunk, BlockPos pos, CableConnections connections) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SyncCableConnectionsPacket(pos, connections));
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

    public static void syncHandlerStatus(BlockPos pos, HandlerData data, Side side, StorageMode mode, boolean enabled) {
        CHANNEL.sendToServer(new SyncHandlerStatus(data.getIdentity(), side, data.getType(), mode, pos, enabled));
    }

    public static void syncHandlerConfigMode(BlockPos pos, StorageMode mode, HandlerConfig config, Side side) {
        CHANNEL.sendToServer(new SyncHandlerConfigMode(pos, config.getBaseData().getIdentity(), side, mode, config.getBaseData().getType()));
    }

    public static void syncDevLog(Chunk chunk, BlockPos pos, String log) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new DevBlockLogSyncPacket(pos, log));
    }

    public static void syncDevEnergy(BlockPos pos, int energy, DevBlockTile.ActiveSignal signal, EnumSet<Side> sides) {
        CHANNEL.sendToServer(new DevBlockEnergySyncPacket(pos, energy, signal, sides));
    }

    public static void syncDevLiquid(BlockPos pos, int liquidPerTick, Fluid liquid, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal) {
        CHANNEL.sendToServer(new DevBlockLiquidSyncPacket(pos, sides, signal, liquidPerTick, liquid));
    }

    public static void syncDevGas(BlockPos pos, int gasPerTick, Fluid gas, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal) {
        CHANNEL.sendToServer(new DevBlockGasSyncPacket(pos, sides, signal, gasPerTick, gas));
    }

    public static void syncDevItem(BlockPos pos, List<ItemStack> inv, EnumSet<Side> sides, DevBlockTile.ActiveSignal signal) {
        CHANNEL.sendToServer(new DevBlockItemSyncPacket(pos, sides, signal, inv));
    }

    public static void onPacketDeparted(Chunk chunk, BlockPos pos, ICablePacket packet, int index) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SyncCablePacket(packet, pos, index, false));
    }

    public static void onPacketArrived(Chunk chunk, BlockPos pos, ICablePacket packet, int index) {
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SyncCablePacket(packet, pos, index, true));
    }

    public static void sendContainerButtonClicked(int widgetId, int buttonId) {
        CHANNEL.sendToServer(new ContainerButtonClicked(widgetId, buttonId));
    }

    public static void syncTaggableSlotState(int slotId, boolean enabled) {
        CHANNEL.sendToServer(new SyncToggleableButtonState(slotId, enabled));
    }

    public static void syncObjectHolder(ServerPlayerEntity player, int holderId, ObjectHolder<?> holder) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncObjectHolder(holderId, holder));
    }
}
