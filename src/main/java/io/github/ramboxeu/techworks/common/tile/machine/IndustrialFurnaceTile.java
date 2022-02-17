package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.IndustrialFurnaceContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.heat.IHeater;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.recipe.IndustrialSmeltingRecipe;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IndustrialFurnaceTile extends BaseMachineTile {
    private static final int COOLING_RATE = 1;
    private static final int HEATING_RATE = 10;

    private final Map<BlockPos, IHeater> heaters = new HashMap<>(6);
    private final ItemStackHandler inv;
    private final ItemHandlerData invPrimaryData;
    private final ItemHandlerData invSecondaryData;
    private final RecipeWrapper recipeInv;
    private final ItemStackHandler outputInv;
    private final ItemHandlerData outputInvData;
    private IndustrialSmeltingRecipe cachedRecipe;
    private boolean shouldCheck = true;
    private boolean working;
    private int heat;
    private int extractedHeat;
    private int requiredHeat;
    private int temperature;

    public IndustrialFurnaceTile() {
        super(TechworksTiles.INDUSTRIAL_FURNACE.get());

        inv = new ItemStackHandler(2){
            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
        invPrimaryData = machineIO.getHandlerData(inv, 0, 1, MachineIO.INPUT);
        invSecondaryData = machineIO.getHandlerData(inv, 1, 2, MachineIO.INPUT);
        recipeInv = new RecipeWrapper(inv);

        outputInv = new ItemStackHandler(){
            @Override
            protected void onContentsChanged(int slot) {
                shouldCheck = true;
            }
        };
        outputInvData = machineIO.getHandlerData(outputInv, MachineIO.OUTPUT);

        components = new ComponentStorage.Builder().build();
    }

    @Override
    protected void onFirstTick() {
        for (Direction dir : Direction.values()) {
            BlockPos pos = this.pos.offset(dir);
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IHeater) {
                heaters.put(pos, (IHeater) tile);
            }
        }
    }

    @Override
    protected void serverTick() {
        if (shouldCheck) {
            if (checkRecipe() && outputInv.insertItem(0, cachedRecipe.getRecipeOutput(), true).isEmpty()) {
                working = true;
                requiredHeat = cachedRecipe.getHeat();
                temperature = cachedRecipe.getTemperature();
            } else {
                working = false;
                extractedHeat = 0;
            }

            setWorkingState(working);
            shouldCheck = false;
        }

        int receivedHeat = heaters.values().stream().mapToInt(heater -> heater.extractHeat(true)).sum();

        if (receivedHeat < heat) {
            heat = Math.max(0, heat - COOLING_RATE);
        } else {
            heat = Math.min(receivedHeat, heat + HEATING_RATE);
        }

        if (heat >= temperature) {
            if (working && standbyMode.canWork() && redstoneMode.canWork(world.isBlockPowered(pos))) {
                if (extractedHeat < requiredHeat) {
                    extractedHeat += receivedHeat;
                }

                if (extractedHeat >= requiredHeat) {
                    extractedHeat = 0;
                    working = false;

                    outputInv.insertItem(0, cachedRecipe.getCraftingResult(recipeInv), false);
                }
            }
        } else {
            if (working) {
                shouldCheck = true;
                extractedHeat = 0;
                setWorkingState(false);
            }
        }
    }

    private boolean checkRecipe() {
        if (world != null) {
            if (cachedRecipe != null && cachedRecipe.matches(recipeInv, world)) {
                return true;
            }

            Optional<IndustrialSmeltingRecipe> recipe = world.getRecipeManager().getRecipe(TechworksRecipes.INDUSTRIAL_SMELTING.get(), recipeInv, world);

            if (recipe.isPresent()) {
                cachedRecipe = recipe.get();
                extractedHeat = 0;
                return true;
            }
        }

        cachedRecipe = null;
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.INDUSTRIAL_FURNACE.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
        return new IndustrialFurnaceContainer(id, inv, this);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Inv", inv.serializeNBT());
        tag.put("OutputInv", outputInv.serializeNBT());
        tag.putInt("ExtractedHeat", extractedHeat);
        tag.putInt("Heat", heat);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        inv.deserializeNBT(tag.getCompound("Inv"));
        outputInv.deserializeNBT(tag.getCompound("OutputInv"));
        extractedHeat = tag.getInt("ExtractedHeat");
        heat = tag.getInt("Heat");

        super.read(state, tag);
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return ItemUtils.collectContents(super.getDrops(), inv, outputInv);
    }

    public int getHeat() {
        return heat;
    }

    public int getExtractedHeat() {
        return extractedHeat;
    }

    public int getRequiredHeat() {
        return requiredHeat;
    }

    public int getTemperature() {
        return temperature;
    }

    public ItemHandlerData getInvPrimaryData() {
        return invPrimaryData;
    }

    public ItemHandlerData getInvSecondaryData() {
        return invSecondaryData;
    }

    public ItemHandlerData getOutputInvData() {
        return outputInvData;
    }

    public void onNeighbourChange(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IHeater) {
            heaters.put(pos, (IHeater) tile);
        } else {
            heaters.remove(pos);
        }
    }
}
