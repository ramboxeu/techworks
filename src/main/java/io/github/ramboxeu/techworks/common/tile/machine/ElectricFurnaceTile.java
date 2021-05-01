package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ElectricFurnaceTile extends BaseMachineTile {
    private int workTime = 0;
    private boolean isWorking = false;

    private final ItemStackHandler inv;
    private final ItemHandlerData invData;

    private final ItemStackHandler outputInv;
    private final ItemHandlerData outputInvData;

    private final RecipeWrapper recipeInv;

    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private FurnaceRecipe cachedRecipe;
    private int cookTime;
    private int energy;
    private int experience;

    private final LiquidTank tank = new LiquidTank(5000);
    private final LiquidHandlerData tankData;

    private int handlerIndex;

    private List<EnergyHandlerData> energyHandlerDataList;

    public ElectricFurnaceTile() {
        super(TechworksTiles.ELECTRIC_FURNACE.get());

        inv = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();

                if (!checkRecipe()) {
                    workTime = 0;
                    isWorking = false;
                }

                update();
            }
        };
        invData = machineIO.getHandlerData(inv);

        outputInv = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
        outputInvData = machineIO.getHandlerData(outputInv);

        recipeInv = new RecipeWrapper(inv);

        battery = new EnergyBattery(5000, 100);
        batteryData = machineIO.getHandlerData(battery);

        tankData = machineIO.getHandlerData(tank);
    }

    public boolean checkRecipe() {
        if (world != null) {
            if (cachedRecipe != null && cachedRecipe.matches(recipeInv, world)) {
                return true;
            } else {
                Optional<FurnaceRecipe> recipe = world.getRecipeManager().getRecipe(/*Registration.TECHWORKS_SMELTING_RECIPE*/ IRecipeType.SMELTING, recipeInv, world);

                cachedRecipe = recipe.orElse(null);
                return recipe.isPresent();
            }
        }

        return false;
    }

    public void update() {
        if (cachedRecipe != null) {
//            energy = cachedRecipe.getEnergy();
            energy = 100;
            cookTime = cachedRecipe.getCookTime();

            Techworks.LOGGER.debug("Energy: {}, CookTime: {}, Experience: {}", energy, cookTime, cachedRecipe.getExperience());
        }
    }

    @Override
    protected void onFirstTick() {
        super.onFirstTick();

        if (!checkRecipe()) {
            workTime = 0;
            isWorking = false;
        }

        update();
    }

    @Override
    protected void serverTick() {
        if (!isWorking && checkRecipe()) {
            update();

            if (outputInv.insertItem(0, cachedRecipe.getCraftingResult(recipeInv), true).isEmpty() /*&& battery.extractEnergy(ENERGY, true) == ENERGY*/) {
                workTime = 0;
                isWorking = true;
            }
        }

        if (isWorking) {
            if (workTime == cookTime) {
                isWorking = false;
                outputInv.insertItem(0, cachedRecipe.getCraftingResult(recipeInv), false);
                experience += cachedRecipe.getExperience();
            } else {
                workTime++;
                battery.extractEnergy(energy / cookTime, false);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        Utils.writeInvToNbt(nbt, "Inv", inv);
        Utils.writeInvToNbt(nbt, "OutputInv", outputInv);
        nbt.put("Battery", battery.serializeNBT());
        nbt.putInt("WorkTime", workTime);
        nbt.putBoolean("IsWorking", isWorking);

        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        Utils.readInvFromNbt(nbt, "Inv", inv);
        Utils.readInvFromNbt(nbt, "OutputInv", outputInv);
        battery.deserializeNBT(nbt);
        workTime = nbt.getInt("WorkTime");
        isWorking = nbt.getBoolean("IsWorking");

        super.read(state, nbt);
    }

    @Override
    protected ITextComponent getComponentsGuiName() {
        return null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.electric_furnace");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new ElectricFurnaceContainer(id, inventory, this, machineIO.createDataMap(), IWorldPosCallable.of(world, pos));
    }

    public EnergyHandlerData getBatteryData() {
        return batteryData;
    }

    public ItemHandlerData getInvData() {
        return invData;
    }

    public ItemHandlerData getOutputInvData() {
        return outputInvData;
    }

    public ItemStackHandler getInventory() {
        return inv;
    }

    public ItemStackHandler getOutputInv() {
        return outputInv;
    }

    public LiquidHandlerData getTankData() {
        return tankData;
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getEnergy() {
        return battery.getEnergyStored();
    }

    public int resetXP() {
        int xp = experience;
        experience = 0;
        return xp;
    }
}
