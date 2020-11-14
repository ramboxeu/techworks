package io.github.ramboxeu.techworks.common.util.machineio;

import io.github.ramboxeu.techworks.common.capability.impl.EnergyHandlerContainer;
import io.github.ramboxeu.techworks.common.capability.impl.FluidHandlerContainer;
import io.github.ramboxeu.techworks.common.capability.impl.ItemHandlerContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public enum InputType {
    NONE(null, null, null),
    ITEM(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ItemHandlerContainer.class, IItemHandler.class),
    LIQUID(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, FluidHandlerContainer.class, IFluidTank.class),
    GAS(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, FluidHandlerContainer.class, IFluidTank.class),
    ENERGY(CapabilityEnergy.ENERGY, EnergyHandlerContainer.class, IEnergyStorage.class);

    private final Capability<?> cap;
    private final Class<?> handlerClass;
    private final Class<?> dataHolderClass;

    InputType(Capability<?> cap, Class<?> handlerClass, Class<?> dataHolderClass) {
        this.cap = cap;
        this.handlerClass = handlerClass;
        this.dataHolderClass = dataHolderClass;
    }

    /**
     * Checks if a handler is valid for given type, in other words if it implements interface give by the capability
     * @return {@code true} when handler is valid, always {@code false} for type NONE
     */
    public boolean isValidHandler(Object handler) {
        if (this != NONE && handler != null) {
            return handlerClass.isAssignableFrom(handler.getClass());
        }

        return false;
    }

    /**
     * Checks if a given object is a valid data holder for {@link}.
     * @return {@code ture} when holder is valid, always {@code false} for type NONE
     */
    @Deprecated
    public boolean isValidDataHolder(Object dataHolder) {
        if (this != NONE) {
            return dataHolderClass.isAssignableFrom(dataHolder.getClass());
        }

        return false;
    }

    /**
     * Converts capability to input type
     * @param cap capability to convert from
     * @return input type for given capability, if
     */
    public static InputType fromCap(@Nullable Capability<?> cap) {
        for (InputType type : values()) {
            if (type.cap == cap) {
                return type;
            }
        }

        return NONE;
    }

    public Class<?> getContainerClass() {
        return handlerClass;
    }
}
