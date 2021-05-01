package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTags;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Predicates;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BoilerTile extends BaseMachineTile {
    private final IItemHandler fuelInventory;

    private FluidTank waterTank = new FluidTank(0, stack -> stack.getFluid().isIn(FluidTags.WATER)) {
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private FluidTank steamTank = new FluidTank(0, stack -> stack.getFluid().isIn(TechworksFluidTags.STEAM)){
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private boolean isWaterTankPresent = false;
    private boolean isSteamTankPresent = false;

    private int burnTime = 0;
    private int fuelBurnTime = 0;
    private int workTime = 0;
    private int steamTime = 0;
    private boolean isWorking;
    private boolean isBurning;

    private Direction next = null;

    public BoilerTile() {
        super(TechworksTiles.BOILER.get());

//        machineIO = MachineIO.create(
//                PortConfig.create(Type.GAS, steamTank),
//                PortConfig.create(Type.LIQUID, waterTank)
//        );

//        machineIO = new MachineIO(this::getFacing);

        fuelInventory = new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return Predicates.isFuel(stack);
            }
        };
    }

    @Override
    protected void serverTick() {
//        boolean flag = true;
//
//        if (!isWaterTankPresent) {
//            flag = false;
//        }
//
//        if (!isSteamTankPresent) {
//            flag = false;
//        }
//
//        if (!boilingComponent.isPresent()) {
//            flag = false;
//            workTime = 0;
//        }
//
//        if (flag) {
//
//            if (!isBurning) {
//                int burnTime = ForgeHooks.getBurnTime(fuelInventory.extractItem(0, 1, true));
//                int totalBurnTime = fuelInventory.getStackInSlot(0).getCount() * burnTime;
//
//                if (burnTime > 0 && (canWork(waterTank, steamTank, boilingComponent) || workTime > 0) && totalBurnTime >= boilingComponent.getWorkTime() - workTime) {
//                    fuelInventory.extractItem(0, 1, false);
//                    isBurning = true;
//                    fuelBurnTime = burnTime;
//                } else {
//                    this.burnTime = 0;
//                    workTime = 0;
//                    steamTime = 0;
//                    isWorking = false;
//                }
//            } else {
//                if (burnTime == fuelBurnTime) {
//                    isBurning = false;
//                    fuelBurnTime = 0;
//                    burnTime = 0;
//
//                    if (ForgeHooks.getBurnTime(fuelInventory.extractItem(0, 1, true)) <= 0 || workTime == boilingComponent.getWorkTime()) {
//                        if (steamTime == boilingComponent.calcWorkTime()) {
//                            steamTank.fill(new FluidStack(TechworksFluids.STEAM.get(), boilingComponent.getSteamAmount()), IFluidHandler.FluidAction.EXECUTE);
//                        }
//
//                        workTime = 0;
//                        steamTime = 0;
//                        isWorking = false;
//                    }
//                } else {
//                    burnTime++;
//
//                    if (!isWorking) {
//                        int totalBurnTime = (fuelInventory.getStackInSlot(0).getCount() * fuelBurnTime) + (fuelBurnTime - burnTime) + 1;
////                        Techworks.LOGGER.debug("TotalBurnTime: {} | Water: [{}]", totalBurnTime, Utils.stringifyFluidStack(waterTank.getFluidInTank(0)));
//
//                        if (canWork(waterTank, steamTank, boilingComponent) && totalBurnTime >= boilingComponent.getWorkTime() ) {
////                            Techworks.LOGGER.debug("Consumed water");
//                            waterTank.drain(boilingComponent.getWaterAmount(), IFluidHandler.FluidAction.EXECUTE);
//                            isWorking = true;
//                            workTime++;
//                            steamTime++;
//                        }
//                    } else {
//                        if (workTime == boilingComponent.getWorkTime()) {
//                            if (steamTime == boilingComponent.calcWorkTime()) {
//                                steamTank.fill(new FluidStack(TechworksFluids.STEAM.get(), boilingComponent.getSteamAmount()), IFluidHandler.FluidAction.EXECUTE);
//                            }
//
//                            isWorking = false;
//                            workTime = 0;
//                            steamTime = 0;
//                        } else {
//                            if (steamTime == boilingComponent.calcWorkTime()) {
//                                steamTank.fill(new FluidStack(TechworksFluids.STEAM.get(), boilingComponent.getSteamAmount()), IFluidHandler.FluidAction.EXECUTE);
//                                steamTime = 1;
//                            } else {
//                                steamTime++;
//                            }
//
//                            workTime++;
//                        }
//                    }
//
//                }
//            }

//            next = Utils.distributeFluid(steamTank, next, this);
//        }
    }

    @Override
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            ItemStack handStack = player.getHeldItem(handIn);

            LazyOptional<IFluidHandlerItem> bucket = handStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

            if (bucket.isPresent()) {
                IFluidHandlerItem bucketTank = Utils.unpack(bucket);
                int maxDrain = waterTank.getTankCapacity(0) - waterTank.getFluidInTank(0).getAmount();

                if (bucketTank.drain(maxDrain, IFluidHandler.FluidAction.SIMULATE).getFluid().isIn(FluidTags.WATER)) {
                    waterTank.fill(bucketTank.drain(maxDrain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    player.setHeldItem(handIn, bucketTank.getContainer());
                    markDirty();

                    return ActionResultType.SUCCESS;
                }
            }
        }

        return ActionResultType.PASS;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("FuelBurnTime", fuelBurnTime);
        compound.putInt("BurnTime", burnTime);
        compound.putInt("WorkTime", workTime);
        compound.putInt("SteamTime", steamTime);
        compound.putBoolean("IsWorking", isWorking);
        compound.putBoolean("IsBurning", isBurning);

        compound.put("FuelInv", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(fuelInventory, null));
        return super.write(compound);
    }


    @Override
    @ParametersAreNonnullByDefault
    public void read(BlockState state, CompoundNBT compound) {
        fuelBurnTime = compound.getInt("FuelBurnTime");
        burnTime = compound.getInt("BurnTime");
        workTime = compound.getInt("WorkTime");
        steamTime = compound.getInt("SteamTime");
        isWorking = compound.getBoolean("IsWorking");
        isBurning = compound.getBoolean("IsBurning");

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(fuelInventory, null, compound.get("FuelInv"));
        super.read(state, compound);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.boiler");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BoilerContainer(id, playerInventory, this, machineIO.createDataMap());
    }

    @Override
    protected ITextComponent getComponentsGuiName() {
        return new TranslationTextComponent("container.techworks.boiler_components");
    }

    public int getFuelBurnTime() {
        return fuelBurnTime;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getWaterStorage() {
        return waterTank.getTankCapacity(0);
    }

    public int getSteamStorage() {
        return steamTank.getTankCapacity(0);
    }

    public FluidStack getWater() {
        return waterTank.getFluidInTank(0);
    }

    public FluidStack getSteam() {
        return steamTank.getFluidInTank(0);
    }

    public IItemHandler getFuelInventory() {
        return fuelInventory;
    }
}
