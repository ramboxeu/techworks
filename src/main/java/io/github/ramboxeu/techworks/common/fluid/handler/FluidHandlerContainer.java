package io.github.ramboxeu.techworks.common.fluid.handler;

import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.FluidHandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.handler.IHandlerContainer;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/*
 * This handler is not saved, nor it saves its children. This means handlers should be saved separately.
 */
public class FluidHandlerContainer implements IFluidHandler, IHandlerContainer {
    private final List<FluidHandlerConfig> tanks;

    public FluidHandlerContainer() {
        tanks = new ArrayList<>();
    }

    public void addHandlers(FluidHandlerConfig... tanks) {
        addHandlers(Arrays.asList(tanks));
    }

    public void addHandlers(List<FluidHandlerConfig> tanks) {
        for (FluidHandlerConfig tank : tanks) {
            addHandler(tank);
        }
    }

    public FluidHandlerConfig addHandler(FluidHandlerConfig tank) {
        FluidHandlerConfig existing = Utils.getExistingConfig(tanks, tank.getBaseData());

        if (existing != null) {
            return existing;
        }

        tanks.add(tank);
        return tank;
    }

    @Override
    public HandlerConfig remove(HandlerData data) {
        return tanks.stream().filter(config -> config.getBaseData() == data).findFirst().map(config -> { tanks.remove(config); return config; } ).orElse(null);
    }

    @Override
    public void setStorageMode(HandlerData data, StorageMode mode) {
        tanks.stream().filter(config -> config.getBaseData() == data).findFirst().ifPresent(config -> config.setMode(mode));
    }

    @Override
    public List<HandlerConfig> getConfigs() {
        return Collections.unmodifiableList(tanks);
    }

    @Override
    public int getTanks() {
        return tanks.size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tanks.get(tank).getTank().getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return tanks.get(tank).getTank().getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return tanks.get(tank).getTank().isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty())
            return 0;

        int maxFill = resource.getAmount();
        int filled = 0;

        for (Iterator<IFluidTank> it = getFluidTankStream(StorageMode::canInput).filter(tank -> tank.isFluidValid(resource)).iterator(); it.hasNext(); ) {
            IFluidTank tank = it.next();
            resource.shrink(filled);
            filled += tank.fill(resource, action);

            if (filled == maxFill) {
                break;
            }
        }

        return filled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int maxDrain = resource.getAmount();
        int drained = 0;

        for (Iterator<IFluidTank> it = getFluidTankStream(StorageMode::canOutput).filter(tank -> tank.getFluid().getFluid() == resource.getFluid()).iterator(); it.hasNext(); ) {
            IFluidTank tank = it.next();
            resource.shrink(drained);
            drained += tank.drain(resource, action).getAmount();

            if (drained == maxDrain) {
                break;
            }
        }

        return new FluidStack(resource.getFluid(), drained);
    }

    private int totalCapacity(Fluid fluid) {
        return getFluidTankStream(mode -> true).filter(tank -> tank.getFluid().getFluid() == fluid).mapToInt(IFluidTank::getFluidAmount).sum();
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        Optional<Fluid> fluidOptional = getFluidTankStream(StorageMode::canOutput).map(tank -> tank.getFluid().getFluid()).max(Comparator.comparingInt(this::totalCapacity));

        if (fluidOptional.isPresent()) {
            Fluid fluid = fluidOptional.get();

            int drained = 0;
            int drain = maxDrain;
            for (Iterator<IFluidTank> it = getFluidTankStream(StorageMode::canOutput).filter(tank -> tank.getFluid().getFluid() == fluid).iterator(); it.hasNext(); ) {
                IFluidTank tank = it.next();
                drained += tank.drain(drain -= drained, action).getAmount();

                if (drained == maxDrain) {
                    break;
                }
            }

            return new FluidStack(fluid, drained);
        }

        return FluidStack.EMPTY;
    }

    private Stream<IFluidTank> getFluidTankStream(Predicate<StorageMode> modePredicate) {
        return tanks.stream().filter(config -> modePredicate.test(config.getMode())).map(FluidHandlerConfig::getTank);
    }
}
