package io.github.ramboxeu.techworks.common.util.cable.item;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.cable.network.BaseCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.network.ICableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.network.ICableNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.network.NetworkType;

import java.util.UUID;

public class ItemNetworkHolder implements ICableNetworkHolder {

    private ItemCableNetwork network;
    private UUID id;

    public ItemNetworkHolder(ItemCableNetwork network, UUID id) {
        this.network = network;
        this.id = id;
    }

    public ItemCableNetwork get() {
        return network;
    }

    @Override
    public BaseCableNetwork getNetwork() {
        return network;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public NetworkType getType() {
        return NetworkType.ITEM;
    }

    @Override
    public void update(UUID id, ICableNetwork network) {
        if (network instanceof ItemCableNetwork) {
            this.id = id;
            this.network = (ItemCableNetwork) network;
        } else {
            if (network != null) {
                Techworks.LOGGER.warn("Tried setting network of type {}, but was expecting {}. Network id: {}", network.getClass().getSimpleName(), ItemCableNetwork.class.getSimpleName(), id);
            }

            Techworks.LOGGER.warn("Null network");
        }
    }
}
