package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.OreWasherContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.recipe.OreWashingRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class OreWasherTile extends BaseMachineTile {

    private static final Random RANDOM = new Random();

    private final LiquidTank waterTank;
    private final LiquidHandlerData waterTankData;

    private final ItemStackHandler inv;
    private final ItemHandlerData invData;

    private final ItemStackHandler outputInv;
    private final ItemHandlerData outputInvData;

    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private final RecipeWrapper recipeInv;
    private OreWashingRecipe cachedRecipe;
    private boolean shouldCheck;
    private boolean isWorking;
    private boolean extractedWater;
    private int extractedEnergy;

    public OreWasherTile() {
        super(TechworksTiles.ORE_WASHER.get());

        waterTank = new LiquidTank(){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isIn(FluidTags.WATER);
            }

            @Override
            protected void onContentsChanged() {
                shouldCheck = true;
            }
        };
        waterTankData = machineIO.getHandlerData(waterTank);

        inv = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
        invData = machineIO.getHandlerData(inv);

        outputInv = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
        outputInvData = machineIO.getHandlerData(outputInv);

        battery = new EnergyBattery() {
            @Override
            protected void onContentsChanged() {
                shouldCheck = true;
            }
        };
        batteryData = machineIO.getHandlerData(battery);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.LIQUID_STORAGE.get(), waterTank)
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .build();

        recipeInv = new RecipeWrapper(inv);
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.ORE_WASHER.text();
    }

    @Override
    protected void workTick() {
        if (shouldCheck) {
            if (checkRecipe() && ItemUtils.canInsertItems(outputInv, cachedRecipe.getRecipeOutputs()) && battery.getEnergyStored() >= 4000 - extractedEnergy && waterTank.getFluidAmount() >= 125) {
                isWorking = true;
            } else {
                isWorking = false;
                extractedEnergy = 0;
                extractedWater = false;
                markDirty();
            }

            setWorkingState(isWorking);
            shouldCheck = false;
        }

        if (isWorking) {
            if (extractedEnergy < 4000) {
                extractedEnergy += battery.extractEnergy(20, false);
            }

            if (!extractedWater) {
                waterTank.drain(125, IFluidHandler.FluidAction.EXECUTE, true);
                extractedWater = true;
            }

            if (extractedEnergy >= 4000) {
                isWorking = false;
                shouldCheck = true;
                extractedEnergy = 0;
                extractedWater = false;

                inv.extractItem(0, 1, false);
                ItemUtils.insertItems(outputInv, cachedRecipe.getCraftingResults(outputInv, RANDOM), false);
                markDirty();
            }
        }
    }

    private boolean checkRecipe() {
        if (cachedRecipe != null && cachedRecipe.matches(recipeInv, world)) {
            return true;
        }

        Optional<OreWashingRecipe> recipe = world.getRecipeManager().getRecipe(TechworksRecipes.ORE_WASHING.get(), recipeInv, world);

        if (recipe.isPresent()) {
            cachedRecipe = recipe.get();
            return true;
        }

        return false;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Inv", inv.serializeNBT());
        tag.put("OutputInv", outputInv.serializeNBT());
        tag.put("Battery", battery.serializeNBT());
        tag.put("WaterTank", waterTank.serializeNBT());
        tag.put("Components", components.serializeNBT());
        tag.putInt("ExtractedEnergy", extractedEnergy);
        tag.putBoolean("IsWorking", isWorking);
        tag.putBoolean("ExtractedWater", extractedWater);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        inv.deserializeNBT(tag.getCompound("Inv"));
        outputInv.deserializeNBT(tag.getCompound("OutputInv"));
        battery.deserializeNBT(tag.getCompound("Battery"));
        waterTank.deserializeNBT(tag.getCompound("WaterTank"));
        components.deserializeNBT(tag.getCompound("Components"));
        extractedEnergy = tag.getInt("ExtractedEnergy");
        isWorking = tag.getBoolean("IsWorking");
        extractedWater = tag.getBoolean("ExtractedWater");

        super.read(state, tag);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new OreWasherContainer(id, playerInv, this);
    }

    public LiquidHandlerData getWaterTankData() {
        return waterTankData;
    }

    public ItemHandlerData getInvData() {
        return invData;
    }

    public ItemHandlerData getOutputInvData() {
        return outputInvData;
    }

    public EnergyHandlerData getBatteryData() {
        return batteryData;
    }

    public int getExtractedEnergy() {
        return extractedEnergy;
    }
}
