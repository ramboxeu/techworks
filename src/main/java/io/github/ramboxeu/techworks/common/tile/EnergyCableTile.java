package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.HandlerStorage;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.cable.energy.EnergyNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.network.ICableNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.network.IEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.NetworkType;
import io.github.ramboxeu.techworks.common.util.cable.network.TransferType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCableTile extends BaseCableTile {

    private final HandlerStorage<IEnergyStorage> handlers = new HandlerStorage<>(CapabilityEnergy.ENERGY);
    private EnergyNetworkHolder network;

    public EnergyCableTile() {
        super(TechworksTiles.ENERGY_CABLE.getTileType(), NetworkType.ENERGY);
    }

    @Override
    protected void setNetwork(ICableNetworkHolder network) {
        super.setNetwork(network);

        if (network instanceof EnergyNetworkHolder)
            this.network = (EnergyNetworkHolder) network;
    }

    @Override
    protected boolean isTileValid(Direction side, TileEntity tile) {
        return PredicateUtils.isEnergyHandler(side, tile);
    }

    @Override
    protected HandlerStorage<?> getHandlerStorage() {
        return handlers;
    }

    @Override
    protected void extract() {
        for (HandlerStorage.Entry<IEnergyStorage> entry : handlers.entries()) {
            if (connections.getMode(entry.getSide()).canInput()) {
                int amount = entry.getHandler().extractEnergy(10000, false);
                IEndpointNode node = network.get().getEndpoint(entry.getTile());
                network.get().transfer(TransferType.NORMAL, amount, this, node);
            }
        }
    }

    @Override
    public int getTransferTime() {
        return 1;
    }
}
