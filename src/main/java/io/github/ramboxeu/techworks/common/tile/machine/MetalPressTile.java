package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.MetalPressContainer;
import io.github.ramboxeu.techworks.common.capability.HandlerStorage;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.lang.TranslationKey;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.recipe.MetalPressingRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public class MetalPressTile extends BaseMachineTile {
    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;

    private final ItemStackHandler inv;
    private final ItemHandlerData invData;

    private final ItemStackHandler outputInv;
    private final ItemHandlerData outputInvData;

    private final RecipeWrapper recipeInv;
    private MetalPressingRecipe cachedRecipe;
    private boolean shouldCheck;
    private boolean isWorking;
    private int extractedEnergy;
    private Mode mode;

    public MetalPressTile() {
        super(TechworksTiles.METAL_PRESS.get());

        battery = new EnergyBattery() {
            @Override
            protected void onContentsChanged() {
                shouldCheck = true;
            }
        };
        batteryData = machineIO.getHandlerData(battery, MachineIO.INPUT | MachineIO.ALL);

        inv = new ItemStackHandler() {
            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
        invData = machineIO.getHandlerData(inv, MachineIO.INPUT);

        outputInv = new ItemStackHandler() {
            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
        outputInvData = machineIO.getHandlerData(outputInv, MachineIO.OUTPUT);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .build();
        handlers.enable(HandlerStorage.ENERGY | HandlerStorage.ITEM);

        mode = Mode.GEAR;
        recipeInv = new RecipeWrapper(inv);
    }

    @Override
    protected void workTick() {
        if (shouldCheck) {
            if (checkRecipe() && outputInv.insertItem(0, cachedRecipe.getRecipeOutput(), true).isEmpty() && battery.getEnergyStored() >= 4000 - extractedEnergy) {
                isWorking = true;
            } else {
                isWorking = false;
                extractedEnergy = 0;
                markDirty();
            }

            setWorkingState(isWorking);
            shouldCheck = false;
        }

        if (isWorking) {
            if (extractedEnergy < 4000) {
                extractedEnergy += battery.extractEnergy(20, false);
            }

            if (extractedEnergy >= 4000) {
                isWorking = false;
                shouldCheck = true;
                extractedEnergy = 0;

                inv.extractItem(0, cachedRecipe.getCount(inv.getStackInSlot(0)), false);
                outputInv.insertItem(0, cachedRecipe.getCraftingResult(recipeInv), false);
                markDirty();
            }
        }
    }

    private boolean checkRecipe() {
        if (world != null) {
            if (cachedRecipe != null && cachedRecipe.getMode() == mode && cachedRecipe.matches(recipeInv, world)) {
                return true;
            }

            Optional<MetalPressingRecipe> optional = world.getRecipeManager().getRecipesForType(TechworksRecipes.METAL_PRESSING.get()).stream()
                    .filter(recipe -> recipe.getMode() == mode && recipe.matches(recipeInv, world))
                    .findFirst();

            if (optional.isPresent()) {
                cachedRecipe = optional.get();
                extractedEnergy = 0;
                return true;
            }
        }

        cachedRecipe = null;
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.METAL_PRESS.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new MetalPressContainer(id, playerInv, this);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Inv", inv.serializeNBT());
        tag.put("OutputInv", outputInv.serializeNBT());
        tag.put("Battery", battery.serializeNBT());
        tag.put("Components", components.serializeNBT());
        tag.putInt("ExtractedEnergy", extractedEnergy);
        tag.putBoolean("IsWorking", isWorking);
        NBTUtils.serializeEnum(tag, "Mode", mode);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        inv.deserializeNBT(tag.getCompound("Inv"));
        outputInv.deserializeNBT(tag.getCompound("OutputInv"));
        battery.deserializeNBT(tag.getCompound("Battery"));
        components.deserializeNBT(tag.getCompound("Components"));
        extractedEnergy = tag.getInt("ExtractedEnergy");
        isWorking = tag.getBoolean("IsWorking");
        mode = NBTUtils.deserializeEnum(tag, "Mode", Mode.class);

        super.read(state, tag);
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return ItemUtils.collectContents(super.getDrops(), inv, outputInv);
    }

    public void cycleMode(boolean backwards) {
        setMode(backwards ? mode.previous() : mode.next());
        TechworksPacketHandler.syncMetalPressMode(pos, mode);
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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        shouldCheck = true;
    }

    public enum Mode {
        GEAR(0, TranslationKeys.GEAR),
        PLATE(16, TranslationKeys.PLATE),
        WIRE(32, TranslationKeys.WIRE),
        DOUBLE_PLATE(48, TranslationKeys.DOUBLE_PLATE);

        private final int iconOffset;
        private final TranslationKey name;

        Mode(int iconOffset, TranslationKey name) {
            this.iconOffset = iconOffset;
            this.name = name;
        }

        public int getIconOffset() {
            return iconOffset;
        }

        public TranslationKey getName() {
            return name;
        }

        public Mode next() {
            switch (this) {
                case GEAR:
                    return PLATE;
                case PLATE:
                    return WIRE;
                case WIRE:
                    return DOUBLE_PLATE;
                case DOUBLE_PLATE:
                    return GEAR;
            }

            throw new AssertionError();
        }

        public Mode previous() {
            switch (this) {
                case GEAR:
                    return DOUBLE_PLATE;
                case PLATE:
                    return GEAR;
                case WIRE:
                    return PLATE;
                case DOUBLE_PLATE:
                    return WIRE;
            }

            throw new AssertionError();
        }
    }
}
