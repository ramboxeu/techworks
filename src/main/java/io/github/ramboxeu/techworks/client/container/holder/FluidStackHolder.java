package io.github.ramboxeu.techworks.client.container.holder;

import io.github.ramboxeu.techworks.client.container.ObjectReferenceHolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public abstract class FluidStackHolder extends ObjectReferenceHolder {
    @Override
    public CompoundNBT serialize(Object value) {
        return ((FluidStack) value).writeToNBT(new CompoundNBT());
    }

    @Override
    public Object deserialize(CompoundNBT tag) {
        return FluidStack.loadFluidStackFromNBT(tag);
    }

    @Override
    protected boolean isSame(Object old, Object current) {
        FluidStack a = (FluidStack) old;
        FluidStack b = (FluidStack) current;
        return a.getFluid() == b.getFluid() && a.getAmount() == b.getAmount();
    }
}
