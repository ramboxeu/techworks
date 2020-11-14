package io.github.ramboxeu.techworks.common.util.machineio;

import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.handler.IHandlerContainer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MachinePort {
    public static final MachinePort DISABLED = new MachinePort();

    private LazyOptional<IItemHandler> itemHolder;
    private LazyOptional<IFluidHandler> liquidHolder;
    private LazyOptional<IFluidHandler> gasHolder;
    private LazyOptional<IEnergyStorage> energyHolder;

    MachinePort() {
        itemHolder = LazyOptional.empty();
        liquidHolder = LazyOptional.empty();
        gasHolder = LazyOptional.empty();
        energyHolder = LazyOptional.empty();
    }

    public LazyOptional<IItemHandler> getItemHolder() {
        return itemHolder;
    }

    public LazyOptional<IEnergyStorage> getEnergyHolder() {
        return energyHolder;
    }

    public LazyOptional<IFluidHandler> getLiquidHolder() {
        return liquidHolder;
    }

    public LazyOptional<IFluidHandler> getGasHolder() {
        return gasHolder;
    }

    public void setItemHolder(LazyOptional<IItemHandler> itemHolder) {
        this.itemHolder = itemHolder;
    }

    public void setLiquidHolder(LazyOptional<IFluidHandler> liquidHolder) {
        this.liquidHolder = liquidHolder;
    }

    public void setGasHolder(LazyOptional<IFluidHandler> gasHolder) {
        this.gasHolder = gasHolder;
    }

    public void setEnergyHolder(LazyOptional<IEnergyStorage> energyHolder) {
        this.energyHolder = energyHolder;
    }

    private List<HandlerConfig> getConfigs(LazyOptional<?> holder) {
        return holder.<List<HandlerConfig>>map(handler -> handler instanceof IHandlerContainer ? ((IHandlerContainer) handler).getConfigs() : Collections.emptyList()).orElse(Collections.emptyList());
    }

    public List<HandlerConfig> getItemConfigs() {
        return getConfigs(itemHolder);
    }

    public List<HandlerConfig> getEnergyConfigs() {
        return getConfigs(energyHolder);
    }

    public List<HandlerConfig> getLiquidConfigs() {
        return getConfigs(liquidHolder);
    }

    public List<HandlerConfig> getGasConfigs() {
        return getConfigs(gasHolder);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void setHolder(LazyOptional<?> holder, InputType type) {
        if (holder != null && holder.isPresent()) {
            if (!type.isValidHandler(holder.orElse(null))) {
                throw new IllegalArgumentException("Supplied optional is not valid for " + type.name() + " input type");
            }
        }

        switch (type) {
            case ITEM:
                itemHolder = (LazyOptional<IItemHandler>) holder;
                break;
            case LIQUID:
                liquidHolder = (LazyOptional<IFluidHandler>) holder;
                break;
            case GAS:
                gasHolder = (LazyOptional<IFluidHandler>) holder;
                break;
            case ENERGY:
                energyHolder = (LazyOptional<IEnergyStorage>) holder;
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> LazyOptional<T> getHolder(InputType type) {
        switch (type) {
            case ITEM:
                return (LazyOptional<T>) itemHolder;
            case LIQUID:
                return (LazyOptional<T>) liquidHolder;
            case GAS:
                return (LazyOptional<T>) gasHolder;
            case ENERGY:
                return (LazyOptional<T>) energyHolder;
            default:
                return null;
        }
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }

    public void invalidate() {
        itemHolder.invalidate();
        liquidHolder.invalidate();
        gasHolder.invalidate();
        energyHolder.invalidate();
    }
}
