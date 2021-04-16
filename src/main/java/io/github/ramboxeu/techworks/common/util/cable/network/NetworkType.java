package io.github.ramboxeu.techworks.common.util.cable.network;

import io.github.ramboxeu.techworks.common.util.cable.energy.EnergyCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.energy.EnergyWrapperEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.fluid.FluidCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.fluid.FluidWrapperEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemCableNetwork;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemWrapperEndpointNode;

import java.util.UUID;

public enum NetworkType {
    ITEM(ItemWrapperEndpointNode::new, ItemCableNetwork::new),
    LIQUID(FluidWrapperEndpointNode::liquid, FluidCableNetwork::liquid),
    GAS(FluidWrapperEndpointNode::gas, FluidCableNetwork::gas),
    ENERGY(EnergyWrapperEndpointNode::new, EnergyCableNetwork::new);

    private final IWrapperEndpointNode.Factory wrapperFactory;
    private final ICableNetwork.Factory networkFactory;

    NetworkType(IWrapperEndpointNode.Factory wrapperFactory, ICableNetwork.Factory networkFactory) {
        this.wrapperFactory = wrapperFactory;
        this.networkFactory = networkFactory;
    }

    public IWrapperEndpointNode.Factory getWrapperFactory() {
        return wrapperFactory;
    }

    public ICableNetwork createNetwork(UUID id) {
        return networkFactory.create(id);
    }
}
