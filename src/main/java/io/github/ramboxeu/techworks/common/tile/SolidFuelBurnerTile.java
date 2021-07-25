package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.client.container.SolidFuelBurnerContainer;
import io.github.ramboxeu.techworks.common.heat.IHeatTransmitter;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolidFuelBurnerTile extends BaseTechworksTile implements INamedContainerProvider {
    // TODO: Heat-up

    private static final int MAX_HEAT = 1000;
    private static final int HEAT_DECAY = 10;

    private final ItemStackHandler fuelInv;
    private final LazyOptional<IItemHandler> fuelInvHolder;

    private boolean shouldCheck;
    private boolean isWorking;
    private int burnTime;
    private int elapsedTime;
    private int totalHeat;
    private int heatGeneration;

    public SolidFuelBurnerTile() {
        super(TechworksTiles.SOLID_FUEL_BURNER.get());

        fuelInv = new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return ForgeHooks.getBurnTime(stack) > 0;
            }

            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };

        fuelInvHolder = LazyOptional.of(() -> fuelInv);
    }

    @Override
    protected void serverTick() {
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
                int heat = totalHeat / burnTime;
                elapsedTime++;

//                if (heatGeneration < heat) {
//                    heatGeneration += heat / 20;
//                }
//
//                if (heatGeneration > heat) {
//                    heatGeneration -= HEAT_DECAY;
//                }
//
//                if (heatGeneration == heat) {
//                    // distribute
//                }

                for (Direction side : Direction.values()) {
                    TileEntity tile = world.getTileEntity(pos.offset(side));

                    if (tile instanceof IHeatTransmitter) {
                        ((IHeatTransmitter) tile).receiveHeat(heat);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return fuelInvHolder.cast();

        return super.getCapability(cap);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("FuelInv", fuelInv.serializeNBT());
        tag.putInt("ElapsedTime", elapsedTime);
        tag.putInt("StoredHeat", heatGeneration);
        tag.putBoolean("IsWorking", isWorking);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("ElapsedTime", elapsedTime);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        fuelInv.deserializeNBT(tag.getCompound("FuelInv"));
        elapsedTime = tag.getInt("ElapsedTime");
        heatGeneration = tag.getInt("StoredHeat");
        isWorking = tag.getBoolean("IsWorking");
        burnTime = tag.getInt("BurnTime");
        elapsedTime = tag.getInt("ElapsedTime");

        if (burnTime <= 0) {
            isWorking = false;
            shouldCheck = true;
        }

        super.read(state, tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.SOLID_FUEL_BURNER.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new SolidFuelBurnerContainer(id, playerInv, this);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        fuelInvHolder.invalidate();
    }

    public ItemStackHandler getFuelInv() {
        return fuelInv;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public int getStoredHeat() {
        return heatGeneration;
    }
}
