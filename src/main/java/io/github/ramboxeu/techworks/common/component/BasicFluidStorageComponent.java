package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.registry.ComponentProviders;
import io.github.ramboxeu.techworks.common.registry.ComponentTypes;
import io.github.ramboxeu.techworks.common.util.FluidStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Random;

public class BasicFluidStorageComponent extends EventEmitter implements IComponent {
    Fluid fluid;
    int amount;
    int counter;

    public BasicFluidStorageComponent(IComponentList<?> list) {
        fluid = Fluids.EMPTY;
        amount = 0;
        Techworks.LOG.info("Creating fluid storage");
    }

    @Override
    public ComponentType getType() {
        return ComponentTypes.FLUID_STORAGE_COMPONENT;
    }

    @Override
    public IComponentProvider getProvider() {
        return ComponentProviders.BASIC_FLUID_STORAGE;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public void tick() {
        if (counter >= 20) {
            Techworks.LOG.info("1 second passed");
            if (fluid.equals(Fluids.WATER)) {
                fluid = Fluids.LAVA;
            } else {
                fluid = Fluids.WATER;
            }
            amount = new Random().nextInt(1000);
            updateAll(new FluidStack(fluid, amount));
            counter = 0;
        } else {
            counter++;
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return new CompoundTag();
    }

    @Override
    public void fromTag(CompoundTag tag) {

    }

    @Override
    public CompoundTag serialize(CompoundTag tag, Object value) {
        FluidStack fluidStack = (FluidStack) value;
        CompoundTag fluidTag = new CompoundTag();
        fluidTag.putInt("amount", fluidStack.getAmount());
        fluidTag.putString("fluid", Registry.FLUID.getId(fluidStack.getFluid()).toString());
        tag.put("FluidStack", fluidTag);
        return tag;
    }

    @Override
    public Object deserialize(CompoundTag tag) {
        CompoundTag fluidTag = tag.getCompound("FluidStack");
        int amount = fluidTag.getInt("amount");
        Fluid fluid = Registry.FLUID.get(new Identifier(fluidTag.getString("fluid")));
        return new FluidStack(fluid, amount);
    }
}
