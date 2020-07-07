package io.github.ramboxeu.techworks.client.container;

import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.inventory.InventoryBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public abstract class AbstractMachineContainer extends Container {
    protected IItemHandler playerInventory;
    private LazyOptional<IItemHandler> inventory;
    private InventoryBuilder builder;
    protected BaseMachineTile machineTile;
    private int energy;

    protected AbstractMachineContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, BaseMachineTile machineTile) {
        super(containerType, id);

        this.playerInventory = new InvWrapper(playerInventory);
        this.inventory = machineTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

//        if (machineTile.hasFluidHandler()) {
//            this.trackInt(new IntReferenceHolder() {
//                @Override
//                public int get() {
//                    return getFluid().getAmount();
//                }
//
//                @Override
//                public void set(int amount) {
//                    machineTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluidHandler -> {
//                        ((FluidTank) fluidHandler).setFluid(new FluidStack(Fluids.WATER, amount));
//                    });
//                }
//            });
//        }
//
//        if (machineTile.hasGasHandler()) {
//            this.trackInt(new IntReferenceHolder() {
//                @Override
//                public int get() {
//                    return getGas();
//                }
//
//                @Override
//                public void set(int amount) {
//                    machineTile.getCapability(CapabilityGas.GAS).ifPresent(gasHandler -> {
//                        ((GasHandler) gasHandler).setAmountStored(amount);
//                    });
//                }
//            });
//        }
//
//        if (machineTile.hasEnergyStorage()) {
//            this.trackInt(new IntReferenceHolder() {
//                @Override
//                public int get() {
//                    energy = machineTile.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
//                    return energy;
//                }
//
//                @Override
//                public void set(int e) {
//                    energy = e;
//                }
//            });
//        }

        this.builder = new InventoryBuilder();

        this.layoutPlayerSlots();

        this.machineTile = machineTile;

        this.inventory.ifPresent(itemHandler ->  {
            this.layoutSlots(this.builder);
            for (SlotItemHandler slot : this.builder.build(itemHandler)) {
                this.addSlot(slot);
            }
        });
    }

    @Override
    public abstract boolean canInteractWith(PlayerEntity playerIn);

    private void layoutPlayerSlots() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new SlotItemHandler(playerInventory, i, 8 + i *18, 142));
        }
    }

    protected void layoutSlots(InventoryBuilder builder) {}

    public int getGas() {
        return this.machineTile.getCapability(CapabilityGas.GAS).map(IGasHandler::getAmountStored).orElse(0);
    }

    public int getEnergy() {
        return this.energy;
    }

    public FluidStack getFluid() {
        return this.machineTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(fluidHandler -> fluidHandler.getFluidInTank(0)).orElse(new FluidStack(Fluids.EMPTY, 100));
    }
}
