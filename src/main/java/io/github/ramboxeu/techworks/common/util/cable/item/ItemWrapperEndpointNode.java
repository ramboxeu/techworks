package io.github.ramboxeu.techworks.common.util.cable.item;

import io.github.ramboxeu.techworks.common.util.cable.network.ICablePacket;
import io.github.ramboxeu.techworks.common.util.cable.network.INode;
import io.github.ramboxeu.techworks.common.util.cable.network.IWrapperEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.PacketType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

public class ItemWrapperEndpointNode implements IWrapperEndpointNode {

    private final TileEntity tile;
    private final Map<Direction, INode> neighbors;
    private final Map<Direction, IItemHandler> handlers;

    public ItemWrapperEndpointNode(TileEntity tile) {
        this.tile = tile;
        this.neighbors = new EnumMap<>(Direction.class);
        this.handlers = new EnumMap<>(Direction.class);
    }

    @Override
    public TileEntity getTileEntity() {
        return tile;
    }

    @Override
    public boolean isEmpty() {
        for (IItemHandler handler : handlers.values()) {
            for (int i = 0; i < handler.getSlots(); i++) {
                if (!handler.getStackInSlot(i).isEmpty())
                    return false;
            }
        }

        return true;
    }

    @Override
    public boolean isFull() {
        for (IItemHandler handler : handlers.values()) {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.getCount() < stack.getMaxStackSize())
                    return false;
            }
        }

        return true;
    }

    @Override
    public Map<Direction, INode> getNeighboursMap() {
        return neighbors;
    }

    @Override
    public BlockPos getPosition() {
        return tile.getPos();
    }

    @Override
    public void addNeighbour(INode node, Direction dir) {
        neighbors.put(dir.getOpposite(), node);

        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir)
                .ifPresent(handler -> handlers.put(dir.getOpposite(), handler));
    }

    @Override
    public void removeNeighbour(INode node) {
        for (Iterator<Map.Entry<Direction, INode>> iter = neighbors.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Direction, INode> entry = iter.next();

            if (entry.getValue() == node) {
                handlers.remove(entry.getKey());
                iter.remove();
            }
        }
    }

    @Override
    public void removeNeighbour(Direction side) {
        neighbors.remove(side.getOpposite());
        handlers.remove(side.getOpposite());
    }

    @Override
    public Object insert(ICablePacket packet, Direction side) {
        if (packet.getType() == PacketType.ITEM) {
            IItemHandler handler = handlers.get(side);

            if (handler != null) {
                ItemStack stack = (ItemStack) packet.getPayload();
                return insertItem(handler, stack);
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof TileEntity)
            return tile == obj;

        if (obj instanceof ItemWrapperEndpointNode)
            return tile == ((ItemWrapperEndpointNode) obj).tile;

        return false;
    }

    private static ItemStack insertItem(IItemHandler handler, ItemStack stack) {
        ItemStack rest = stack;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.isEmpty() || slotStack.isItemEqual(rest)) {
                rest = handler.insertItem(i, rest, false);
                break;
            }
        }

        return rest;
    }
}
