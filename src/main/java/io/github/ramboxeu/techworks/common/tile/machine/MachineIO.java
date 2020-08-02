package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.common.capability.CapsNames;
import io.github.ramboxeu.techworks.common.capability.handler.ConfigurableEnergyHandler;
import io.github.ramboxeu.techworks.common.capability.handler.ConfigurableFluidHandler;
import io.github.ramboxeu.techworks.common.capability.handler.ConfigurableItemHandler;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort.Mode;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort.Type;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.HashMap;
import java.util.Map;

import static io.github.ramboxeu.techworks.common.tile.machine.MachinePort.DISABLED;

public class MachineIO {
    //                                                 down       up      north      south     west      east
    private MachinePort[] ports = new MachinePort[]{ DISABLED, DISABLED, DISABLED, DISABLED, DISABLED, DISABLED };
    private Map<Type, PortConfig> portConfigs;
    private int disabledFaces = 0;

    private MachineIO(HashMap<Type, PortConfig> portConfigs) {
        this.portConfigs = portConfigs;
    }

    public void configurePort(int index, Type type, Mode mode) {
        MachinePort port = ports[index];

        if (port.getType() != type || port.getMode() != mode) {
            if (type == Type.NONE) {
                ports[index] = DISABLED;
            } else {
                PortConfig config = portConfigs.get(type);
                if (config != null) {
                    ports[index] = config.fromMode(mode);
                }
            }
        }
    }

    public void setFaceStatus(Direction face, boolean disabled) {
        if (disabled) {
            disabledFaces |= Utils.getDirectionBinIndex(face);
            configurePort(face.getIndex(), Type.NONE, Mode.NONE);
        } else {
            disabledFaces &= -Utils.getDirectionBinIndex(face) - 1;
        }
    }

    public boolean isFaceDisabled(Direction face) {
        int index = Utils.getDirectionBinIndex(face);
        return (disabledFaces & index) == index;
    }

    public MachinePort getPort(Direction direction) {
        return ports[direction.getIndex()];
    }

    public MachinePort[] getPorts() {
        return ports;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (side != null) {
            if (ports[side.getIndex()].getCapability() == cap) {
                return ports[side.getIndex()].getHolder().cast();
            }
        }

        return null;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();

        for (MachinePort port : ports) {
            CompoundNBT portTag = new CompoundNBT();
            portTag.putString("Type", port.getType().name());
            portTag.putString("Mode", port.getMode().name());
            list.add(portTag);
        }

        nbt.put("Ports", list);
        nbt.putByte("DisabledFaces", (byte) disabledFaces);
        return nbt;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT list = nbt.getList("Ports", Constants.NBT.TAG_COMPOUND);

        disabledFaces = nbt.getByte("DisabledFaces");

        for (int i = 0; i < list.size(); i++) {
            CompoundNBT portTag = list.getCompound(i);
            Type type = Type.valueOf(portTag.getString("Type"));

            if (type != Type.NONE) {
                ports[i] = portConfigs.get(type).fromMode(Mode.valueOf(portTag.getString("Mode")));
            } else {
                ports[i] = DISABLED;
            }
        }
    }

    public static MachineIO create(PortConfig ...portConfigs) {
        HashMap<Type, PortConfig> configMap = new HashMap<>(portConfigs.length);

        for (PortConfig portConfig : portConfigs) {
            configMap.put(portConfig.type, portConfig);
        }

        return new MachineIO(configMap);
    }

    // TYPE => INPUT => OUTPUT => BOTH => TYPE
    public void cyclePort(Direction direction) {
        MachinePort port = ports[direction.getIndex()];

        Type type = port.getType();
        Mode mode = port.getMode();

        Type newType = type;
        Mode newMode = mode;

        if (mode == Mode.BOTH || type == Type.NONE) {
            newMode = Mode.INPUT;

            do {
                newType = Type.next(newType);
            } while (!portConfigs.containsKey(newType) && newType != Type.NONE);
        } else {
            switch (mode) {
                case INPUT:
                    newMode = Mode.OUTPUT;
                    break;
                case OUTPUT:
                    newMode = Mode.BOTH;
                    break;
            }
        }

        configurePort(direction.getIndex(), newType, newMode);
    }

    public static class PortConfig {
        private MachinePort bothPort;
        private MachinePort inputPort;
        private MachinePort outputPort;
        private Type type;

        private static final PortConfig EMPTY = new PortConfig(DISABLED, DISABLED, DISABLED, Type.NONE);

        public PortConfig(MachinePort bothPort, MachinePort inputPort, MachinePort outputPort, Type type) {
            this.bothPort = bothPort;
            this.inputPort = inputPort;
            this.outputPort = outputPort;
            this.type = type;
        }

        private MachinePort fromMode(Mode mode) {
            switch (mode) {
                case INPUT:
                    return inputPort;
                case OUTPUT:
                    return outputPort;
                case BOTH:
                    return bothPort;
            }

            return DISABLED;
        }

        // NONE type should not be used
        public static PortConfig create(Type type, Object holder) {
            Object inputHolder;
            Object outputHolder;
            Capability<?> capability = type.getCapability();

            switch (type) {
                case ITEM:
                    IItemHandlerModifiable stackHandler = (IItemHandlerModifiable)holder;
                    outputHolder = new ConfigurableItemHandler(Mode.OUTPUT, stackHandler);
                    inputHolder = new ConfigurableItemHandler(Mode.INPUT, stackHandler);
                    break;
                case GAS:
                case LIQUID:
                    IFluidHandler fluidHandler = (IFluidHandler) holder;
                    outputHolder = new ConfigurableFluidHandler(Mode.OUTPUT, fluidHandler);
                    inputHolder = new ConfigurableFluidHandler(Mode.INPUT, fluidHandler);
                    break;
                case ENERGY:
                    IEnergyStorage energyStorage = (IEnergyStorage) holder;
                    outputHolder = new ConfigurableEnergyHandler(Mode.OUTPUT, energyStorage);
                    inputHolder = new ConfigurableEnergyHandler(Mode.INPUT, energyStorage);
                default:
                    return EMPTY;
            }

            return new PortConfig(
                    new MachinePort(LazyOptional.of(() -> holder), capability, type, Mode.BOTH),
                    new MachinePort(LazyOptional.of(() -> inputHolder), capability, type, Mode.INPUT),
                    new MachinePort(LazyOptional.of(() -> outputHolder), capability, type, Mode.OUTPUT),
                    type
            );
        }

        private boolean isEmpty() {
            return this == EMPTY;
        }
    }
}
