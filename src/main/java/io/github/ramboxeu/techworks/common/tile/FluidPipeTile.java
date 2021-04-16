package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.registry.TileRegistryObject;
import io.github.ramboxeu.techworks.common.util.FluidUtils;
import io.github.ramboxeu.techworks.common.util.HandlerStorage;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.cable.fluid.FluidNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.network.ICableNetworkHolder;
import io.github.ramboxeu.techworks.common.util.cable.network.IEndpointNode;
import io.github.ramboxeu.techworks.common.util.cable.network.NetworkType;
import io.github.ramboxeu.techworks.common.util.cable.network.TransferType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPipeTile extends BaseCableTile {

    private final HandlerStorage<IFluidHandler> handlers = new HandlerStorage<>(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
    private FluidNetworkHolder network;

    public FluidPipeTile(TileRegistryObject<FluidPipeTile> tileType, NetworkType type) {
        super(tileType.getTileType(), type);
    }

    @Override
    protected void setNetwork(ICableNetworkHolder network) {
        super.setNetwork(network);

        if (network instanceof FluidNetworkHolder)
            this.network = (FluidNetworkHolder) network;
    }

    @Override
    protected void extract() {
        for (HandlerStorage.Entry<IFluidHandler> entry : handlers.entries()) {
            if (connections.getMode(entry.getSide()).canInput()) {
                FluidStack stack = FluidUtils.firstNotEmpty(entry.getHandler(), 1000);

                if (!stack.isEmpty() && network.get().isFluidValid(stack)) {
                    IEndpointNode node = network.get().getEndpoint(entry.getTile());
                    network.get().transfer(TransferType.NORMAL, stack, this, node);
                }
            }
        }
    }

    @Override
    protected boolean isTileValid(Direction side, TileEntity tile) {
        return PredicateUtils.isFluidHandler(side, tile);
    }

    @Override
    protected HandlerStorage<?> getHandlerStorage() {
        return handlers;
    }

    @Override
    public int getTransferTime() {
        return 20; // 1 block per second
    }
}
