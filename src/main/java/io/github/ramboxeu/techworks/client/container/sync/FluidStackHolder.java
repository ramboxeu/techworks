package io.github.ramboxeu.techworks.client.container.sync;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidStackHolder extends ObjectHolder<FluidStack> {
    private final Supplier<FluidStack> getter;
    private final Consumer<FluidStack> setter;

    public FluidStackHolder(Supplier<FluidStack> getter, Consumer<FluidStack> setter) {
        super(ObjectHolderSerializers.FLUID_STACK);
        this.getter = getter;
        this.setter = setter;
    }

    private FluidStackHolder(FluidStack value) {
        super(ObjectHolderSerializers.FLUID_STACK, value);
        getter = () -> FluidStack.EMPTY;
        setter = $ -> {};
    }

    @Override
    public FluidStack get() {
        return getter.get();
    }

    @Override
    public void set(FluidStack value) {
        setter.accept(value);
    }

    @Override
    protected FluidStack copy(FluidStack value) {
        return value.copy();
    }

    @Override
    protected boolean areEqual(FluidStack oldValue, FluidStack newValue) {
        if (oldValue == null) {
            return newValue == null;
        }

        return oldValue.isFluidStackIdentical(newValue);
    }

    public static class Serializer extends ObjectHolderSerializer<FluidStack> {

        public Serializer(int id) {
            super(id);
        }

        @Override
        public void encode(ObjectHolder<FluidStack> stack, PacketBuffer buffer) {
            stack.oldValue.writeToPacket(buffer);
        }

        @Override
        public ObjectHolder<FluidStack> decode(PacketBuffer buffer) {
            return new FluidStackHolder(FluidStack.readFromPacket(buffer));
        }
    }
}
