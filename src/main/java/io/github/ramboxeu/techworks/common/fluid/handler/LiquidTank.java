package io.github.ramboxeu.techworks.common.fluid.handler;

import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.component.LiquidStorageComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class LiquidTank extends FluidHandler implements ILiquidTank, ComponentStorage.ComponentChangeListener<LiquidStorageComponent> {

    public LiquidTank(int capacity) {
        super(capacity);
    }

    public LiquidTank(int capacity, int transfer) {
        super(capacity, transfer);
    }

    public LiquidTank(int capacity, int maxFill, int maxDrain) {
        super(capacity, maxFill, maxDrain);
    }

    @Override
    public boolean isLiquidValid(FluidStack stack) {
        return true;
    }

    @Override
    public void onChange(LiquidStorageComponent component, ItemStack stack) {
        capacity = component.getCapacity();
        if (!fluid.isEmpty()) fluid.setAmount(Math.min(fluid.getAmount(), capacity));
        maxDrain = component.getTransfer();
        maxFill = component.getTransfer();
    }
}
