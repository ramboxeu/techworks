package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.ElectricGrinderContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.recipe.GrinderRecipeType;
import io.github.ramboxeu.techworks.common.recipe.IGrinderRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Optional;

public class ElectricGrinderTile extends BaseMachineTile {
    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private final ItemStackHandler inv;
    private final ItemHandlerData invData;

    private final ItemStackHandler outputInv;
    private final ItemHandlerData outputInvData;

    private final RecipeWrapper recipeInv;

    private boolean shouldCheck;
    private boolean isWorking;

    private GrinderRecipeType type;
    private float energyModifier;
    private int extractedEnergy;
    private int energyCap;
    private int energy;

    private IGrinderRecipe cachedRecipe;

    public ElectricGrinderTile() {
        super(TechworksTiles.ELECTRIC_GRINDER.get());

        battery = new EnergyBattery() {
            @Override
            protected void onContentsChanged() {
                if (!isWorking) {
                    markDirty();
                    shouldCheck = true;
                }
            }
        };
        batteryData = machineIO.getHandlerData(battery);

        inv = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                shouldCheck = true;
            }
        };
        invData = machineIO.getHandlerData(inv);

        outputInv = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                shouldCheck = true;
            }
        };
        outputInvData = machineIO.getHandlerData(outputInv);

        recipeInv = new RecipeWrapper(inv);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .component(TechworksComponents.GRINDING.get(), (component, stack) -> {
                    energyModifier = component.getModifier();
                    energyCap = component.getCap(stack);
                    shouldCheck = true;

                    GrinderRecipeType type = component.getRecipeType();

                    if (type != this.type) {
                        cachedRecipe = null;
                        extractedEnergy = 0;
                    }

                    this.type = type;
                })
                .build();
    }

    @Override
    protected void workTick() {
        if (shouldCheck) {
            if (checkRecipe() && outputInv.insertItem(0, cachedRecipe.getCraftingResult(recipeInv), true).isEmpty() && battery.getEnergyStored() >= energy - extractedEnergy) {
                isWorking = true;
                energy = (int) (cachedRecipe.getEnergy() * energyModifier);
            } else {
                isWorking = false;
                extractedEnergy = 0;
            }

            setWorkingState(isWorking);
            shouldCheck = false;
        }

        if (isWorking) {
            if (extractedEnergy < energy) {
                extractedEnergy += battery.extractEnergy(energyCap, false);
            }

            if (extractedEnergy >= energy) {
                isWorking = false;
                shouldCheck = true;
                extractedEnergy = 0;

                outputInv.insertItem(0, cachedRecipe.getCraftingResult(recipeInv), false);
                inv.extractItem(0, 1, false);

                markDirty();
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Inv", inv.serializeNBT());
        tag.put("OutputInv", outputInv.serializeNBT());
        tag.put("Battery", battery.serializeNBT());
        tag.putInt("ExtractedEnergy", extractedEnergy);
        tag.putBoolean("IsWorking", isWorking);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        inv.deserializeNBT(tag.getCompound("Inv"));
        outputInv.deserializeNBT(tag.getCompound("OutputInv"));
        battery.deserializeNBT(tag.getCompound("Battery"));
        extractedEnergy = tag.getInt("ExtractedEnergy");
        isWorking = tag.getBoolean("IsWorking");

        super.read(state, tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.ELECTRIC_GRINDER.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new ElectricGrinderContainer(id, inventory, this);
    }

    private boolean checkRecipe() {
        if (world != null) {
            if (cachedRecipe != null && cachedRecipe.matches(recipeInv, world)) {
                return true;
            }

            Optional<? extends IGrinderRecipe> recipe = world.getRecipeManager().getRecipe(type.get(), recipeInv, world);

            if (recipe.isPresent()) {
                cachedRecipe = recipe.get();
                extractedEnergy = 0;
                return true;
            }
        }

        cachedRecipe = null;
        return false;
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

    public int getEnergy() {
        return energy;
    }

    public int getExtractedEnergy() {
        return extractedEnergy;
    }
}
