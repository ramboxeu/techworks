package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.ElectricFurnaceContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.recipe.ITechworksSmeltingRecipe;
import io.github.ramboxeu.techworks.common.recipe.TechworksSmeltingRecipe;
import io.github.ramboxeu.techworks.common.recipe.VanillaSmeltingRecipeWrapper;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Optional;

public class ElectricFurnaceTile extends BaseMachineTile {
    private boolean isWorking = false;
    private boolean shouldCheck;

    private final ItemStackHandler inv;
    private final ItemHandlerData invData;

    private final ItemStackHandler outputInv;
    private final ItemHandlerData outputInvData;

    private final RecipeWrapper recipeInv;

    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private ITechworksSmeltingRecipe cachedRecipe;
    private int energyCap;
    private int energy;
    private int extractedEnergy;
    private float experience;
    private float energyModifier;

    public ElectricFurnaceTile() {
        super(TechworksTiles.ELECTRIC_FURNACE.get());

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

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.SMELTING.get(), (smelting, stack) -> {
                    energyModifier = smelting.getModifier();
                    energyCap = smelting.getCap(stack);
                    shouldCheck = true;
                })
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .build();
    }

    private boolean checkRecipe() {
        if (world != null) {
            if (cachedRecipe != null && cachedRecipe.matches(recipeInv, world)) {
                return true;
            } else {
                Optional<TechworksSmeltingRecipe> recipe = world.getRecipeManager().getRecipe(TechworksRecipes.SMELTING.get(), recipeInv, world);

                if (recipe.isPresent()) {
                    cachedRecipe = recipe.get();
                    return true;
                } else {
                    Optional<VanillaSmeltingRecipeWrapper> wrapper = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, recipeInv, world).map(VanillaSmeltingRecipeWrapper::new);
                    cachedRecipe = wrapper.orElse(null);
                    return wrapper.isPresent();
                }
            }
        }

        return false;
    }

    @Override
    protected void buildComponentStorage(ComponentStorage.Builder builder) {

    }

    @Override
    protected void serverTick() {
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
                experience += cachedRecipe.getExperience();
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
        tag.put("Components", components.serializeNBT());
        tag.putInt("ExtractedEnergy", extractedEnergy);
        tag.putFloat("Experience", experience);
        tag.putBoolean("IsWorking", isWorking);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        inv.deserializeNBT(tag.getCompound("Inv"));
        outputInv.deserializeNBT(tag.getCompound("OutputInv"));
        battery.deserializeNBT(tag.getCompound("Battery"));
        components.deserializeNBT(tag.getCompound("Components"));
        extractedEnergy = tag.getInt("ExtractedEnergy");
        experience = tag.getFloat("Experience");
        isWorking = tag.getBoolean("IsWorking");

        super.read(state, tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.ELECTRIC_FURNACE.text();
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

    public int getExtractedEnergy() {
        return extractedEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public float resetXP() {
        float xp = experience;
        experience = 0;
        return xp;
    }
}
