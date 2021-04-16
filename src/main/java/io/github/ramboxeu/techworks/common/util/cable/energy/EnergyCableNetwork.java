package io.github.ramboxeu.techworks.common.util.cable.energy;

import io.github.ramboxeu.techworks.common.util.cable.network.*;
import io.github.ramboxeu.techworks.common.util.cable.pathfinding.Pathfinding;
import net.minecraft.util.Direction;

import java.util.List;
import java.util.UUID;

public class EnergyCableNetwork extends BaseCableNetwork {

    public EnergyCableNetwork(UUID id) {
        super(id);
    }

    public EnergyCableNetwork(UUID id, List<IEndpointNode> endpoints) {
        super(id, endpoints);
    }

    @Override
    public NetworkType getType() {
        return NetworkType.ENERGY;
    }

    @Override
    public void tick() {}

    public void transfer(TransferType type, int energy, INode node, IEndpointNode origin) {
        IEndpointNode endpoint = getTransferTarget(type, origin);

        if (endpoint != null) {
            Direction side = Pathfinding.finalOffset(node, endpoint);
            endpoint.insert(new EnergyPacket(energy), side);
        }
    }
}
