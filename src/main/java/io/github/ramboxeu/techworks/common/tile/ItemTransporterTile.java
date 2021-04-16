package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.HandlerStorage;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemPacket;
import io.github.ramboxeu.techworks.common.util.cable.network.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemTransporterTile extends BaseCableTile {

    private final List<ItemPacket> packets = new ArrayList<>();
    private final HandlerStorage<IItemHandler> handlers = new HandlerStorage<>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    private ItemNetworkHolder network;

    public ItemTransporterTile() {
        super(TechworksTiles.ITEM_TRANSPORTER.getTileType(), NetworkType.ITEM);
    }

    @Override
    protected void setNetwork(ICableNetworkHolder network) {
        super.setNetwork(network);

        if (network instanceof ItemNetworkHolder)
            this.network = (ItemNetworkHolder) network;
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isRemote) // Ticking is used for animations, so we need this, as network ticks only on server
            packets.forEach(ItemPacket::tick);
    }

    @Override
    protected void extract() {
        for (HandlerStorage.Entry<IItemHandler> entry : handlers.entries()) {
            if (connections.getMode(entry.getSide()).canInput()) {
                ItemStack extractedStack = ItemUtils.fromFirstNotEmpty(entry.getHandler(), 4);

                if (!extractedStack.isEmpty()) {
                    IEndpointNode origin = network.get().getEndpoint(entry.getTile());
                    network.get().transfer(TransferType.NORMAL, extractedStack, this, origin, entry.getSide());
                    break;
                }
            }
        }
    }

    @Override
    protected boolean isTileValid(Direction side, TileEntity tile) {
        return PredicateUtils.isItemHandler(side, tile);
    }

    @Override
    public int getTransferTime() {
        return 20; // 1 block per second
    }

    @Override
    public void onPacketArrived(ICablePacket packet) {
        if (packet instanceof ItemPacket) {
            packets.add((ItemPacket) packet);

            if (!world.isRemote)
                TechworksPacketHandler.onPacketArrived(world.getChunkAt(pos), pos, packet, (packets.size() - 1));
        }
    }

    @Override
    public void onPacketDeparted(ICablePacket packet) {
        if (packet instanceof ItemPacket) {
            packets.remove(packet);

            if (!world.isRemote)
                TechworksPacketHandler.onPacketDeparted(world.getChunkAt(pos), pos, packet, packets.size());
        }
    }

    @Override
    protected HandlerStorage<?> getHandlerStorage() {
        return handlers;
    }

    public List<ItemPacket> getPackets() {
        return packets;
    }

    // public static class ItemHandler implements IItemHandler {}
}
