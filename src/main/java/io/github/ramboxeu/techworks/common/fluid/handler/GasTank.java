package io.github.ramboxeu.techworks.common.fluid.handler;

import io.github.ramboxeu.techworks.common.component.GasStorageComponent;
import io.github.ramboxeu.techworks.common.component.IComponentsChangeListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GasTank extends FluidHandler implements IGasTank, IComponentsChangeListener<GasStorageComponent> {

    public GasTank() {
        super(0, 0, 0);
    }

    public GasTank(int capacity) {
        super(capacity);
    }

    public GasTank(int capacity, int transfer) {
        super(capacity, transfer);
    }

    public GasTank(int capacity, int maxFill, int maxDrain) {
        super(capacity, maxFill, maxDrain);
    }

    @Override
    public boolean isGasValid(FluidStack stack) {
        return true;
    }

    @Override
    public void onComponentsChanged(GasStorageComponent component, ItemStack stack) {
        capacity = component.getCapacity();
        if (!fluid.isEmpty()) fluid.setAmount(Math.min(fluid.getAmount(), capacity));
        maxDrain = component.getTransfer();
        maxFill = component.getTransfer();
    }
}
