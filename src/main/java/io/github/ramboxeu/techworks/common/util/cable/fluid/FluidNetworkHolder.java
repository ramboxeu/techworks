package io.github.ramboxeu.techworks.common.util.cable.fluid;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.cable.network.BaseCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.network.ICableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.network.ICableNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.network.NetworkType;

import java.util.UUID;

public class FluidNetworkHolder implements ICableNetworkHolder {

    private FluidCableNetwork network;
    private UUID id;
    private NetworkType type;

    private FluidNetworkHolder(NetworkType type, FluidCableNetwork network, UUID id) {
        this.network = network;
        this.id = id;
        this.type = type;
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
        return type;
    }

    @Override
    public void update(UUID id, ICableNetwork network) {
        if (network instanceof FluidCableNetwork) {
            if (network.getType() == type) {
                this.id = id;
                this.network = (FluidCableNetwork) network;
            } else {
                Techworks.LOGGER.warn("Expected type {}, got {}. Network id: {}", type, network.getType(), id);
            }
        } else {
            if (network != null) {
                Techworks.LOGGER.warn("Tried setting network of type {}, but was expecting {}. Network id: {}", network.getClass().getSimpleName(), FluidCableNetwork.class.getSimpleName(), id);
            }

            Techworks.LOGGER.warn("Null network");
        }
    }

    public FluidCableNetwork get() {
        return network;
    }

    public static FluidNetworkHolder liquid(FluidCableNetwork network, UUID id) {
        return new FluidNetworkHolder(NetworkType.LIQUID, network, id);
    }

    public static FluidNetworkHolder gas(FluidCableNetwork network, UUID id) {
        return new FluidNetworkHolder(NetworkType.GAS, network, id);
    }
}
