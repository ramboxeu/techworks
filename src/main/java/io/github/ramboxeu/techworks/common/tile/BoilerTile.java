package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BoilerTile extends AbstractMachineTile {
    private int cookTime = 0;
    private int counter = 0;
    private boolean isWorking = true;

    public BoilerTile() {
        super(Registration.BOILER_TILE.get(), 1);
    }

    @Override
    void run() {
        this.inventory.ifPresent(inventory -> {
            this.gasHandler.ifPresent(gas -> {
                this.fluidHandler.ifPresent(fluid -> {
                    int burnTime = ForgeHooks.getBurnTime(inventory.getStackInSlot(0));

                    if (!inventory.extractItem(0, 1, true).isEmpty()
                            && fluid.drain(400, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 400) {
                        if (cookTime == burnTime) {
                            inventory.extractItem(0, 1, false);
                            cookTime = 0;
                        } else {
                            cookTime++;
                        }

                        if (counter == 200) {
                            this.gasHandler.ifPresent(tank -> tank.insertGas(Registration.STEAM_GAS.get(), 400, false));
                            this.fluidHandler.ifPresent(tank -> tank.drain(400, IFluidHandler.FluidAction.EXECUTE));
                            counter = 0;
                        } else {
                            counter++;
                        }
                    }
                });
            });
        });
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    boolean canWork() {
        return this.isWorking;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

    @Nullable
    @Override
    protected IItemHandlerModifiable createItemHandler() {
        return new ItemStackHandler(5) {
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

    @Nullable
    @Override
    protected IEnergyStorage createEnergyStorage() {
        return null;
    }

    @Nullable
    @Override
    protected IGasHandler createGasHandler() {
        return new GasHandler(Registration.STEAM_GAS.get(), 400, 10000) {
            @Override
            public void onContentsChanged() {
                markDirty();
            }
        };
    }

    @Nullable
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
        return this.cookTime;
    }

    public int getBurnTime() {
        return ForgeHooks.getBurnTime(this.inventory.map(inventory -> inventory.getStackInSlot(0)).orElse(ItemStack.EMPTY));
    }
}
