package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.client.container.SolidFuelBurnerContainer;
import io.github.ramboxeu.techworks.common.heat.HeaterType;
import io.github.ramboxeu.techworks.common.heat.IHeater;
import io.github.ramboxeu.techworks.common.heat.SolidFuelHeater;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class SolidFuelBurnerTile extends BaseTechworksTile implements INamedContainerProvider, IHeater {
    private static final int COOLING_RATE = 1;
    private static final int HEATING_RATE = 10;

    private final SolidFuelHeater heater;
    private final LazyOptional<IItemHandler> fuelInvHolder;
    private int heat;

    public SolidFuelBurnerTile() {
        super(TechworksTiles.SOLID_FUEL_BURNER.get());

        heater = new SolidFuelHeater();
        fuelInvHolder = LazyOptional.of(heater::getFuelInv);
    }

    @Override
    protected void serverTick() {
        heater.tick();

        if (heater.getBurnTime() <= 0) {
            heat = Math.max(0, heat - COOLING_RATE);
        } else {
            int receivedHeat = heater.extractHeat(false);

            if (receivedHeat > 0) {
                if (heat > receivedHeat) {
                    heat = Math.max(receivedHeat, heat - COOLING_RATE);
                } else {
                    heat = Math.min(receivedHeat, heat + HEATING_RATE);
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
        tag.putInt("Heat", heat);
        tag.put("Heater", heater.serializeNBT());
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        heat = tag.getInt("Heat");
        heater.deserializeNBT(tag.getCompound("Heater"));
        super.read(state, tag);
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
    public ITextComponent getDisplayName() {
        return TranslationKeys.SOLID_FUEL_BURNER.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new SolidFuelBurnerContainer(id, playerInv, this);
    }

    public ItemStackHandler getFuelInv() {
        return heater.getFuelInv();
    }

    public int getBurnTime() {
        return heater.getBurnTime();
    }

    public int getElapsedTime() {
        return heater.getElapsedTime();
    }

    public int getStoredHeat() {
        return heat;
    }

    public Collection<ItemStack> getDrops() {
        return ItemUtils.collectContents(heater.getFuelInv());
    }
}
