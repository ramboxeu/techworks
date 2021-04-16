package io.github.ramboxeu.techworks.common.util.cable.item;

import io.github.ramboxeu.techworks.common.util.cable.network.*;
import io.github.ramboxeu.techworks.common.util.cable.pathfinding.Pathfinder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import java.util.*;

public class ItemCableNetwork extends BaseCableNetwork {

    private final Set<ItemPacket> packets = new HashSet<>();

    public ItemCableNetwork(UUID id) {
        super(id);
    }

    public ItemCableNetwork(UUID id, List<IEndpointNode> endpoints) {
        super(id, endpoints);
    }

    @Override
    public NetworkType getType() {
        return NetworkType.ITEM;
    }

    @Override
    public void tick() {
        packets.forEach(ItemPacket::tick);

        for (Iterator<ItemPacket> iter = packets.iterator(); iter.hasNext(); ) {
            ItemPacket packet = iter.next();

            if (packet.isReady()) {
                if (packet.transport())
                    iter.remove();
            }
        }
    }

    public void transfer(TransferType type, ItemStack stack, INode node, IEndpointNode origin, Direction side) {
        IEndpointNode target = getTransferTarget(type, origin);
        List<Direction> offsets = Pathfinder.findOffsets(node, target);

        if (!offsets.isEmpty()) {
            offsets.add(0, side); // ney

            ItemPacket packet = new ItemPacket(stack, offsets, node, target, this);
            packets.add(packet);
        }
    }
}
