package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.common.capability.InventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.capability.extensions.IInventoryItemStackHandler;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BoilerTile extends AbstractMachineTile {
    private int cookTime = 0;
    private int cachedBurnTime = 0;
    private int counter = 0;
    private boolean isBurning;

    public BoilerTile() {
        super(Registration.BOILER_TILE.get(), 1);
    }

    @Override
    void run() {
        this.inventory.ifPresent(inventory -> {
            this.gasHandler.ifPresent(gas -> {
                this.fluidHandler.ifPresent(fluid -> {
                    int burnTime = ForgeHooks.getBurnTime(inventory.getStackInSlot(0));

                    int fluidAmount = fluid.getFluidInTank(0).getAmount();
                    int gasAmount = gas.getAmountStored();

                    if (!isBurning && burnTime > 0 && fluidAmount >= 400 && gasAmount <= gas.getMaxStorage() - 400 && !inventory.extractItem(0, 1, true).isEmpty()) {
                        inventory.extractItem(0, 1, false);
                        isBurning = true;
                        cachedBurnTime = burnTime;
                        this.getBlockState().with(TechworksBlockStateProperties.RUNNING, true);
                    }

                    if (isBurning) {
                        if (cookTime == cachedBurnTime) {
                            cookTime = 0;
                            cachedBurnTime = 0;
                            isBurning = false;
                            if (burnTime == 0) {
                                counter = 0;
                            }
                            this.getBlockState().with(TechworksBlockStateProperties.RUNNING, false);
                        } else {
                            cookTime++;
                        }

                        if (counter == 200) {
                            if (gas.insertGas(Registration.STEAM_GAS.get(), 400, true) == 400
                                    && fluid.drain(400, IFluidHandler.FluidAction.SIMULATE).getAmount() == 400)
                            {
                                gas.insertGas(Registration.STEAM_GAS.get(), 400, false);
                                fluid.drain(400, IFluidHandler.FluidAction.EXECUTE);
                            }
                            counter = 0;
                        } else {
                            counter++;
                        }
                    } else {
                        this.getBlockState().with(TechworksBlockStateProperties.RUNNING, false);
                    }

                    List<IGasHandler> handlers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        TileEntity te = world.getTileEntity(pos.offset(direction));
                        if (te != null) {
                            te.getCapability(CapabilityGas.GAS, direction.getOpposite()).ifPresent(handlers::add);
                        }
                    }

                    for (int i = handlers.size(); i > 0; i--) {
                        handlers.get(i - 1).insertGas(Registration.STEAM_GAS.get(), gas.extractGas(Registration.STEAM_GAS.get(), gas.getAmountStored() / i, false), false);
                    }
                });
            });
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("counter", counter);
        compound.putInt("cookTime", cookTime);
        compound.putInt("cachedBurnTime", cachedBurnTime);
        compound.putBoolean("isBurning", isBurning);

        return super.write(compound);
    }

    @Override
    boolean canWork() {
        return true;
    }

    @Override
    public void read(CompoundNBT compound) {
        counter = compound.getInt("counter");
        cookTime = compound.getInt("cookTime");
        cachedBurnTime = compound.getInt("cachedBurnTime");
        isBurning = compound.getBoolean("isBurning");

        super.read(compound);
    }

    @Override
    public boolean hasItemHandler() {
        return true;
    }

    @Override
    public boolean hasFluidHandler() {
        return true;
    }

    @Override
    public boolean hasGasHandler() {
        return true;
    }

    @Nullable
    @Override
    protected IInventoryItemStackHandler createItemHandler() {
        return new InventoryItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public int getSlotLimit(int slot) {
                switch (slot) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        return 1;
                    default:
                        return 64;
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return PredicateUtils.isFuel(stack);
                    case 1:
                    case 2:
                        return PredicateUtils.isFluidHandler(stack);
                    default: return false;
                }
            }
        };
    }

    @Override
    protected IGasHandler createGasHandler() {
        return new GasHandler(Registration.STEAM_GAS.get(), 400, 10000) {
            @Override
            public void onContentsChanged() {
                markDirty();
            }
        };
    }

    @Override
    protected IFluidHandler createFluidHandler() {
        return new FluidTank(10000, fluidStack -> fluidStack.getFluid().equals(Fluids.WATER)) {
            @Override
            protected void onContentsChanged() {
                markDirty();
            }
        };
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.boiler");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BoilerContainer(id, playerInventory, this);
    }

    public void handleFluidHandlerInput(ItemStack stack, SlotItemHandler slot) {
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                if (fluidHandler.getFluidInTank(i).isEmpty() || !fluidHandler.getFluidInTank(i).isFluidEqual(new FluidStack(Fluids.WATER, 1000)))
                    return;

                this.fluidHandler.ifPresent(machineFluidHandler -> {
                    int drainAmount = machineFluidHandler.getTankCapacity(0) - machineFluidHandler.getFluidInTank(0).getAmount();
                    if (machineFluidHandler.fill(fluidHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE) > 0) {
                        slot.putStack(fluidHandler.getContainer());
                    }
                });
            }
        });
    }

    public void handleFluidHandlerOutput(ItemStack stack, SlotItemHandler slot) {
        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                if (!fluidHandler.getFluidInTank(i).isEmpty() && !fluidHandler.getFluidInTank(i).getFluid().equals(Fluids.WATER))
                    return;

                int finalI = i;
                this.fluidHandler.ifPresent(machineFluidHandler -> {
                    int fillAmount = fluidHandler.getTankCapacity(finalI) - fluidHandler.getFluidInTank(finalI).getAmount();
                    if (fluidHandler.fill(machineFluidHandler.drain(fillAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE) > 0)
                        slot.putStack(fluidHandler.getContainer());
                });
            }
        });
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getBurnTime() {
        return cachedBurnTime;
    }
}
