package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.component.LiquidStorageComponent;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.fluid.handler.SingletonFluidContainer;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.FluidUtils;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LiquidTankTile extends StorageTile<LiquidStorageComponent> {
    private final LiquidTank tank;
    private final LazyOptional<IFluidHandler> holder;

    public LiquidTankTile() {
        super(TechworksTiles.LIQUID_STORAGE.get(), TechworksComponents.LIQUID_STORAGE.get());

        tank = new LiquidTank() {
            @Override
            protected void onContentsChanged() {
                if (world != null && !world.isRemote)
                    syncTankContents();
            }

            @Override
            public void onComponentsChanged(LiquidStorageComponent component, ItemStack stack) {
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
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.put("Tank", tank.serializeNBT());
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        tank.deserializeNBT(tag.getCompound("Tank"));
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        tank.deserializeNBT(tag.getCompound("Tank"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Tank", tank.serializeNBT());
        return super.write(tag);
    }

    @Override
    protected void onComponentsChanged(LiquidStorageComponent component, ItemStack stack) {
        tank.onComponentsChanged(component, stack);
    }

    @Override
    protected int getAmountStored() {
        return tank.getFluidAmount();
    }

    @Override
    public boolean onRightClick(PlayerEntity player, Hand hand) {
        if (FluidUtils.onHandlerInteraction(player, hand, tank))
            return true;

        return super.onRightClick(player, hand);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        holder.invalidate();
    }

    public FluidStack getTankContents() {
        return tank.getFluid();
    }

    private void syncTankContents() {
        TechworksPacketHandler.syncFluidStorage(world.getChunkAt(pos), pos, tank.getFluid());
    }

    public void setTankContents(FluidStack fluid) {
        tank.setFluid(fluid);
    }

    public float getFillPercentage() {
        return MathUtils.calcProgress(tank.getFluidAmount(), tank.getCapacity());
    }
}
