package io.github.ramboxeu.techworks.common.tile.machine;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class MachinePort {
    private LazyOptional<?> holder;
    private Capability<?> capability;
    private Type type;
    private Mode mode;

    public static final MachinePort DISABLED = new MachinePort(LazyOptional.empty(), null, Type.NONE, Mode.NONE);

    public MachinePort(LazyOptional<?> holder, Capability<?> capability, Type type, Mode mode) {
        this.holder = holder;
        this.capability = capability;
        this.type = type;
        this.mode = mode;
    }

    public Capability<?> getCapability() {
        return capability;
    }

    public LazyOptional<?> getHolder() {
        return holder;
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }

    public Type getType() {
        return type;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Type {
        NONE(null),
        ITEM(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY),
        LIQUID(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY),
        GAS(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY),
        ENERGY(CapabilityEnergy.ENERGY);

        private final Capability<?> capability;

        Type(Capability<?> capability) {
            this.capability = capability;
        }

        public Capability<?> getCapability() {
            return capability;
        }

        public static Type next(Type type) {
            switch (type) {
                case ITEM:
                    return Type.LIQUID;
                case LIQUID:
                    return Type.GAS;
                case GAS:
                    return Type.ENERGY;
                case ENERGY:
                    return Type.NONE;
                default:
                    return Type.ITEM;
            }
        }
    }

    public enum Mode {
        NONE,
        INPUT,
        OUTPUT,
        BOTH;

        public boolean canInput() {
            return this == INPUT || this == BOTH;
        }

        public boolean canOutput() {
            return this == OUTPUT || this == BOTH;
        }
    }
}
