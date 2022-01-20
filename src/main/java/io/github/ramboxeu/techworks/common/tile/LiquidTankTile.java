package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.common.component.LiquidStorageComponent;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.fluid.handler.SingletonFluidContainer;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.MathUtils;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

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
    public ItemStack onRightClick(ItemStack stack, PlayerEntity player) {
        LazyOptional<IFluidHandlerItem> holder = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (holder.isPresent()) {
            IFluidHandlerItem handler = Utils.unpack(holder);

            if (tank.getFluid().isEmpty()) {
                FluidStack drained = handler.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE);

                if (!drained.isEmpty()) {
                    tank.fill(drained, IFluidHandler.FluidAction.EXECUTE, true);
                    return handler.getContainer();
                }
            } else {
                FluidStack query = tank.getFluid().copy();
                query.setAmount(tank.getCapacity() - tank.getFluidAmount());
                FluidStack drained = handler.drain(query, IFluidHandler.FluidAction.EXECUTE);

                if (!drained.isEmpty()) {
                    tank.fill(drained, IFluidHandler.FluidAction.EXECUTE, true);
                    return handler.getContainer();
                }

                query = tank.getFluid().copy();
                int filled = handler.fill(query, IFluidHandler.FluidAction.EXECUTE);

                if (filled > 0) {
                    tank.drain(filled, IFluidHandler.FluidAction.EXECUTE, true);
                    return handler.getContainer();
                }
            }
        }

        return super.onRightClick(stack, player);
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
