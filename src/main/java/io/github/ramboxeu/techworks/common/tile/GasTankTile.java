package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.component.GasStorageComponent;
import io.github.ramboxeu.techworks.common.fluid.handler.GasTank;
import io.github.ramboxeu.techworks.common.fluid.handler.SingletonFluidContainer;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GasTankTile extends StorageTile<GasStorageComponent> {
    private final GasTank tank;
    private final LazyOptional<IFluidHandler> holder;

    public GasTankTile() {
        super(TechworksTiles.GAS_STORAGE.get(), TechworksComponents.GAS_STORAGE.get());

        tank = new GasTank() {
            @Override
            protected void onContentsChanged() {
                syncTankContents();
            }

            @Override
            public void onComponentsChanged(GasStorageComponent component, ItemStack stack) {
                capacity = component.getStorageCapacity();
                if (!fluid.isEmpty()) fluid.setAmount(Math.min(fluid.getAmount(), capacity));
                maxDrain = component.getStorageTransfer();
                maxFill = component.getStorageTransfer();
            }
        };
        holder = LazyOptional.of(() -> new SingletonFluidContainer(tank));
        tank.onComponentsChanged(component, componentStack);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(cap, holder);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("Tank", tank.serializeNBT());
        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT tag, BlockState state) {
        super.readUpdateTag(tag, state);

        tank.deserializeNBT(tag.getCompound("Tank"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Tank", tank.serializeNBT());
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);

        tank.deserializeNBT(tag.getCompound("Tank"));
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        holder.invalidate();
    }

    @Override
    protected void onComponentsChanged(GasStorageComponent component, ItemStack stack) {
        tank.onComponentsChanged(component, stack);
    }

    @Override
    protected int getAmountStored() {
        return tank.getFluidAmount();
    }

    public void setTankContents(FluidStack fluid) {
        tank.setFluid(fluid);
    }

    public FluidStack getTankContents() {
        return tank.getFluid();
    }

    public float getFillPercentage() {
        return tank.getFluidAmount() / (float)tank.getCapacity();
    }

    private void syncTankContents() {
        TechworksPacketHandler.syncFluidStorage(world.getChunkAt(pos), pos, tank.getFluid());
    }
}
