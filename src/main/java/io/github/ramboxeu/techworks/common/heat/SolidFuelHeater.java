package io.github.ramboxeu.techworks.common.heat;

import io.github.ramboxeu.techworks.common.capability.HandlerStorage;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolidFuelHeater implements IConfiguringHeater, INBTSerializable<CompoundNBT> {
    private final ItemStackHandler fuelInv;
    private ItemHandlerData fuelInvData;

    private int heat;
    private boolean shouldCheck;
    private boolean isWorking;
    private int elapsedTime;
    private int burnTime;
    private int totalHeat;

    public SolidFuelHeater() {
        this.fuelInv = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return ForgeHooks.getBurnTime(stack) > 0;
            }

            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
    }

    @Override
    public void tick() {
        if (shouldCheck && !isWorking) {
            if (fuelInv.extractItem(0, 1, true).isEmpty()) {
                elapsedTime = 0;
                burnTime = 0;
                totalHeat = 0;
            } else {
                ItemStack stack = fuelInv.extractItem(0, 1, false);
                int stackBurnTime = ForgeHooks.getBurnTime(stack);

                burnTime = Math.round(stackBurnTime * 0.25f);
                totalHeat = Math.round(stackBurnTime * 37.5f);
                elapsedTime = 0;
                isWorking = true;
            }

            shouldCheck = false;
        }

        if (isWorking) {
            if (elapsedTime == burnTime) {
                isWorking = false;
                shouldCheck = true;
            } else {
                heat = totalHeat / burnTime;
                elapsedTime++;
            }
        }
    }

    @Override
    public int extractHeat(boolean simulate) {
        int heat = this.heat;

        if (!simulate)
            this.heat = 0;

        return heat;
    }

    @Override
    public HeaterType getHeaterType() {
        return HeaterType.SOLID_FUEL;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.put("FuelInv", fuelInv.serializeNBT());
        tag.putInt("ElapsedTime", elapsedTime);
        tag.putBoolean("IsWorking", isWorking);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("ElapsedTime", elapsedTime);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        fuelInv.deserializeNBT(tag.getCompound("FuelInv"));
        elapsedTime = tag.getInt("ElapsedTime");
        isWorking = tag.getBoolean("IsWorking");
        burnTime = tag.getInt("BurnTime");
        elapsedTime = tag.getInt("ElapsedTime");

        if (burnTime <= 0) {
            isWorking = false;
            shouldCheck = true;
        }
    }

    @Override
    public void configure(IHeaterConfigurationStore config) {
        fuelInvData = config.getHandlerData(fuelInv, MachineIO.INPUT);
        config.setHandlerStorageFlags(HandlerStorage.ITEM);
        config.setDropsSupplier(() -> ItemUtils.collectContents(fuelInv));
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public ItemStackHandler getFuelInv() {
        return fuelInv;
    }

    public ItemHandlerData getFuelInvData() {
        return fuelInvData;
    }
}
