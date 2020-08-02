package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.util.ResourceLocation;
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
        NONE(null, "disabled"),
        ITEM(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, "item"),
        LIQUID(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, "liquid"),
        GAS(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, "gas"),
        ENERGY(CapabilityEnergy.ENERGY, "energy");

        private final Capability<?> capability;
        private final ResourceLocation inputSprite;
        private final ResourceLocation outputSprite;
        private final ResourceLocation bothSprite;

        Type(Capability<?> capability, String spriteName) {
            this.capability = capability;
            this.inputSprite = new ResourceLocation(Techworks.MOD_ID, "machine/port/" + spriteName + "_input");
            this.outputSprite = new ResourceLocation(Techworks.MOD_ID, "machine/port/" + spriteName + "_output");
            this.bothSprite = new ResourceLocation(Techworks.MOD_ID, "machine/port/" + spriteName + "_both");
        }

        public Capability<?> getCapability() {
            return capability;
        }

        public ResourceLocation getSpriteLocation(Mode mode) {
            if (this == NONE) return null;
            switch (mode) {
                case INPUT:
                    return inputSprite;
                case OUTPUT:
                    return outputSprite;
                case BOTH:
                    return bothSprite;
                default:
                    return null;
            }
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
