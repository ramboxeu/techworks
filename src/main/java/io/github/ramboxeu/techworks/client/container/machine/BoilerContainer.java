package io.github.ramboxeu.techworks.client.container.machine;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.container.ObjectReferenceHolder;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.tile.machine.BoilerTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.inventory.SlotBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

public class BoilerContainer extends BaseMachineContainer<BoilerTile> {
    private int cookTime;
    private int burnTime;
    private FluidStack waterStack;
    private FluidStack steamStack;

    public BoilerContainer(int id, PlayerInventory playerInventory, BoilerTile tile) {
        super(Registration.BOILER_CONTAINER.get(), id, playerInventory, tile);

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return machineTile.getBurnTime();
            }

            @Override
            public void set(int value) {
                cookTime = value;
            }
        });

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return machineTile.getFuelBurnTime();
            }

            @Override
            public void set(int value) {
                burnTime = value;
            }
        });

        trackObject(new ObjectReferenceHolder() {
            @Override
            public Object get() {
                return machineTile.getWater();
            }

            @Override
            public CompoundNBT serialize(Object value) {
                return ((FluidStack) value).writeToNBT(new CompoundNBT());
            }

            @Override
            public void set(Object value) {
                waterStack = (FluidStack) value;
            }

            @Override
            public Object deserialize(CompoundNBT tag) {
                return FluidStack.loadFluidStackFromNBT(tag);
            }
        });

        trackObject(new ObjectReferenceHolder() {
            @Override
            public Object get() {
                return machineTile.getSteam();
            }

            @Override
            public CompoundNBT serialize(Object value) {
                return ((FluidStack) value).writeToNBT(new CompoundNBT());
            }

            @Override
            public void set(Object value) {
                steamStack = (FluidStack) value;
            }

            @Override
            public Object deserialize(CompoundNBT tag) {
                return FluidStack.loadFluidStackFromNBT(tag);
            }
        });

        IItemHandler fuelInv = machineTile.getFuelInventory();
        for (int i = 0; i < fuelInv.getSlots(); i++) {
            addSlot(new SlotBuilder(fuelInv, i).pos(80, 54).predicate(PredicateUtils::isFuel));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack slotItemStack;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null) {
            slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();
            if (index < 36) {
                if (PredicateUtils.isFuel(itemStack)) {
                    if (!this.mergeItemStack(itemStack, 36, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.mergeItemStack(itemStack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        slot.putStack(itemStack);

        return ItemStack.EMPTY;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public FluidStack getWaterStack() {
        return waterStack;
    }

    public FluidStack getSteamStack() {
        return steamStack;
    }
}
