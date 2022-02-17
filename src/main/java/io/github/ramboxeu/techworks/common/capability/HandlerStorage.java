package io.github.ramboxeu.techworks.common.capability;

import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

public class HandlerStorage {
    public static final int ITEM = 1;
    public static final int LIQUID = 2;
    public static final int GAS = 4;
    public static final int ENERGY = 8;

    private final Map<Direction, Unit> storage;
    private int flags;

    public HandlerStorage() {
        this(0);
    }

    public HandlerStorage(int flags) {
        this.flags = flags;
        storage = new EnumMap<>(Direction.class);
    }

    public void enable(int flags) {
        this.flags |= flags;
    }

    public void disable(int flags) {
        this.flags &= ~flags;
    }

    public void store(Direction side, Capability<?> cap, LazyOptional<?> holder) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (flags & ITEM) != 0) {
            Unit unit = storage.computeIfAbsent(side, s -> new Unit());

            IItemHandler handler = (IItemHandler) subscribe(holder);

            if (handler != null)
                unit.addItem(handler);
        }

        if (cap == CapabilityEnergy.ENERGY && (flags & ENERGY) != 0) {
            Unit unit = storage.computeIfAbsent(side, s -> new Unit());

            IEnergyStorage storage = (IEnergyStorage) subscribe(holder);

            if (storage != null)
                unit.addEnergy(storage);
        }

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (flags & (LIQUID | GAS)) != 0) {
            Unit unit = storage.computeIfAbsent(side, s -> new Unit());

            IFluidHandler handler = (IFluidHandler) subscribe(holder);

            if (handler != null)
                unit.addFluid(handler);
        }
    }

    public boolean isEnabled(int flag) {
        return (flags & flag) != 0;
    }

    private <T> T subscribe(LazyOptional<T> holder) {
        if (holder.isPresent()) {
            T handler = Utils.unpack(holder);
            holder.addListener(h -> {
//                Techworks.LOGGER.debug("Removing {} owned by {}", handler, h);
                for (Unit unit : storage.values()) {
                    unit.remove(handler);
                }
            });
//            Techworks.LOGGER.debug("Added {} owned by {}", handler, holder);

            return handler;
        }

        return null;
    }

    public List<IItemHandler> getItem(Direction side) {
        Unit unit = storage.get(side);

        if (unit != null)
            return unit.item != null ? unit.item : Collections.emptyList();

        return Collections.emptyList();
    }

    public List<IEnergyStorage> getEnergy(Direction side) {
        Unit unit = storage.get(side);

        if (unit != null)
            return unit.energy != null ? unit.energy : Collections.emptyList();

        return Collections.emptyList();
    }

    public List<IFluidHandler> getLiquid(Direction side) {
        Unit unit = storage.get(side);

        if (unit != null)
            return unit.fluid != null ? unit.fluid : Collections.emptyList();

        return Collections.emptyList();
    }

    public List<IFluidHandler> getGas(Direction side) {
        Unit unit = storage.get(side);

        if (unit != null)
            return unit.fluid != null ? unit.fluid : Collections.emptyList();

        return Collections.emptyList();
    }

    public static class Unit {
        private List<IItemHandler> item;
        private List<IEnergyStorage> energy;
        private List<IFluidHandler> fluid;

        private void addItem(IItemHandler handler) {
            if (item == null)
                item = new ArrayList<>();

            if (!item.contains(handler))
                item.add(handler);
        }

        public void addEnergy(IEnergyStorage storage) {
            if (energy == null)
                energy = new ArrayList<>();

            if (!energy.contains(storage))
                energy.add(storage);
        }

        public void addFluid(IFluidHandler handler) {
            if (fluid == null)
                fluid = new ArrayList<>();

            if (!fluid.contains(handler))
                fluid.add(handler);
        }

        public void remove(Object o) {
            if (item != null && item.remove(o)) return;
            if (energy != null && energy.remove(o)) return;
            if (fluid != null) fluid.remove(o);
        }
    }
}
