package io.github.ramboxeu.techworks.common.util.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

// Current component system is not flexible enough to allow for per-machine FluidStacks checks.
@SuppressWarnings("ConstantConditions")
public class FluidWrapper implements IFluidHandlerItem {
    private LazyOptional<IFluidHandlerItem> handler;
    private Predicate<FluidStack> predicate;

    public FluidWrapper(LazyOptional<IFluidHandlerItem> handler, Predicate<FluidStack> predicate) {
        this.handler = handler;
        this.predicate = predicate;
    }

    @Override
    public int getTanks() {
        return handler.isPresent() ? handler.orElse(null).getTanks() : 0;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return handler.isPresent() ? handler.orElse(null).getFluidInTank(tank) : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return handler.isPresent() ? handler.orElse(null).getTankCapacity(tank) : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return handler.isPresent() && (handler.orElse(null).isFluidValid(tank, stack) && predicate.test(stack));
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return handler.isPresent() && predicate.test(resource) ? handler.orElse(null).fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource.getAmount(), action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return handler.isPresent() ? handler.orElse(null).drain(maxDrain, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack getContainer() {
        return handler.isPresent() ? handler.orElse(null).getContainer() : ItemStack.EMPTY;
    }

    public void setHandler(LazyOptional<IFluidHandlerItem> handler) {
        this.handler = handler;
    }
}
