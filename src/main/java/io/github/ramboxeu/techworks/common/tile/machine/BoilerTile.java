package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler.Slot;
import io.github.ramboxeu.techworks.api.component.base.BaseBoilingComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseGasStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseLiquidStorageComponent;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.common.component.IComponentsContainerProvider;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.capability.EmptyTankItem;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BoilerTile extends BaseMachineTile implements IComponentsContainerProvider {
    private final IItemHandler fuelInventory;

    private LazyOptional<IFluidHandlerItem> waterTank = LazyOptional.empty();
    private LazyOptional<IFluidHandlerItem> steamTank = LazyOptional.empty();
    private Optional<BaseBoilingComponent> boilingComponent = Optional.empty();

    private int burnTime = 0;
    private int fuelBurnTime = 0;
    private int workTime = 0;
    private int steamTime = 0;
    private boolean isWorking;
    private boolean isBurning;

    private Direction next = null;

    public BoilerTile() {
        super(Registration.BOILER_TILE.get(), new ComponentStackHandler.Builder(3)
                        .slot(0, new Slot()
                                .predicate(stack -> stack.getItem() instanceof BaseLiquidStorageComponent)
                                .texture(new ResourceLocation(Techworks.MOD_ID, "textures/gui/slot/liquid_storage_component_overlay.png"))
                        )
                        .slot(1, new Slot()
                                .predicate(stack -> stack.getItem() instanceof BaseBoilingComponent)
                                .texture(new ResourceLocation(Techworks.MOD_ID, "textures/gui/slot/boiling_component_overlay.png"))
                        )
                        .slot(2, new Slot()
                                .predicate(stack -> stack.getItem() instanceof BaseGasStorageComponent)
                                .texture(new ResourceLocation(Techworks.MOD_ID, "textures/gui/slot/gas_storage_component_overlay.png"))
                        )
        );

        fuelInventory = new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return PredicateUtils.isFuel(stack);
            }
        };


        this.capabilities[1] = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        this.optionals[1] = LazyOptional.of(() -> fuelInventory);
    }

    @Override
    protected void serverTick() {
        boolean flag = true;

        if (!waterTank.isPresent()) {
            flag = false;
            Techworks.LOGGER.warn("Water tank is not present!");
        }

        if (!steamTank.isPresent()) {
            flag = false;
            Techworks.LOGGER.warn("Steam tank is not present!");
        }

        if (!boilingComponent.isPresent()) {
            flag = false;
            workTime = 0;
            Techworks.LOGGER.warn("Boiling component is not present!");
        }

        if (flag) {
            IFluidHandlerItem steamTank = this.steamTank.orElse(EmptyTankItem.INSTANCE);
            BaseBoilingComponent boilingComponent = this.boilingComponent.get();

            if (!isBurning) {
                int burnTime = ForgeHooks.getBurnTime(fuelInventory.extractItem(0, 1, true));
                IFluidHandlerItem waterTank = this.waterTank.orElse(EmptyTankItem.INSTANCE);

                if (burnTime > 0 && canWork(waterTank, steamTank, boilingComponent)) {
                    fuelInventory.extractItem(0, 1, false);
                    isBurning = true;
                    fuelBurnTime = burnTime;
                }
            } else {
                if (burnTime == fuelBurnTime) {
                    isBurning = false;
                    burnTime = 0;
                    fuelBurnTime = 0;

                    if (ForgeHooks.getBurnTime(fuelInventory.extractItem(0, 1, true)) <= 0) {
                        workTime = 0;
                        steamTime = 0;
                    }
                } else {
                    burnTime++;
                }

                if (!isWorking) {
                    IFluidHandlerItem waterTank = this.waterTank.orElse(EmptyTankItem.INSTANCE);

                    if (canWork(waterTank, steamTank, boilingComponent)) {
                        waterTank.drain(boilingComponent.getWaterAmount(), IFluidHandler.FluidAction.EXECUTE);
                        isWorking = true;
                    }
                } else {
                    if (workTime == boilingComponent.getWorkTime()) {
                        isWorking = false;
                        workTime = 0;
                        steamTime = 0;
                    } else {
                        if (steamTime == boilingComponent.calcWorkTime()) {
                            steamTank.fill(new FluidStack(TechworksFluids.STEAM.getLeft().get(), boilingComponent.getSteamAmount()), IFluidHandler.FluidAction.EXECUTE);
                            steamTime = 0;
                        } else {
                            steamTime++;
                        }

                        workTime++;
                    }
                }
            }

            next = Utils.distributeFluid(steamTank, next, this);
        }
    }

    private boolean canWork(IFluidHandler waterTank, IFluidHandler steamTank, BaseBoilingComponent boilingComponent) {
        int totalSteamAmount = boilingComponent.getSteamAmount() * boilingComponent.calcWorkTime();

        return (waterTank.drain(boilingComponent.getWaterAmount(), IFluidHandler.FluidAction.SIMULATE).getAmount() == boilingComponent.getWaterAmount() &&
                steamTank.fill(new FluidStack(TechworksFluids.STEAM.getLeft().get(), totalSteamAmount), IFluidHandler.FluidAction.SIMULATE) == totalSteamAmount);
    }

    @Override
    protected void markComponentsDirty(boolean force) {
        if ((world != null && !world.isRemote) || force) {
            waterTank = components.getStackInSlot(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
            steamTank = components.getStackInSlot(2).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

            ItemStack boilingStack = components.getStackInSlot(1);
            boilingComponent = boilingStack.getItem() instanceof BaseBoilingComponent ? Optional.of((BaseBoilingComponent) boilingStack.getItem()) : Optional.empty();
        }
    }

    @Override
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            ItemStack handStack = player.getHeldItem(handIn);

            LazyOptional<IFluidHandlerItem> bucket = handStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

            if (bucket.isPresent() && waterTank.isPresent()) {
                IFluidHandlerItem bucketTank = bucket.orElse(EmptyTankItem.INSTANCE);
                IFluidHandlerItem waterTank = this.waterTank.orElse(EmptyTankItem.INSTANCE);

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
        compound.put("ComponentInv", components.serializeNBT());

        return super.write(compound);
    }


    @Override
    // a. k. a read
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        fuelBurnTime = compound.getInt("FuelBurnTime");
        burnTime = compound.getInt("BurnTime");
        workTime = compound.getInt("WorkTime");
        steamTime = compound.getInt("SteamTime");
        isWorking = compound.getBoolean("IsWorking");
        isBurning = compound.getBoolean("IsBurning");

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(fuelInventory, null, compound.get("FuelInv"));
        components.deserializeNBT(compound.getCompound("ComponentInv"));
        markComponentsDirty(true);

        super.func_230337_a_(state, compound);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.boiler");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BoilerContainer(id, playerInventory, this);
    }


    @Override
    public ComponentStackHandler getComponentsStackHandler() {
        return components;
    }

    @Override
    public ITextComponent getComponentsDisplayName() {
        // TODO: translation
        return new StringTextComponent("Boiler Components");
    }

    public void handleFluidHandlerInput(ItemStack stack, SlotItemHandler slot) {
//        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
//            for (int i = 0; i < fluidHandler.getTanks(); i++) {
//                if (fluidHandler.getFluidInTank(i).isEmpty() || !fluidHandler.getFluidInTank(i).isFluidEqual(new FluidStack(Fluids.WATER, 1000)))
//                    return;
//
//                this.fluidHandler.ifPresent(machineFluidHandler -> {
//                    int drainAmount = machineFluidHandler.getTankCapacity(0) - machineFluidHandler.getFluidInTank(0).getAmount();
//                    if (machineFluidHandler.fill(fluidHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE) > 0) {
//                        slot.putStack(fluidHandler.getContainer());
//                    }
//                });
//            }
//        });
    }

    public void handleFluidHandlerOutput(ItemStack stack, SlotItemHandler slot) {
//        stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
//            for (int i = 0; i < fluidHandler.getTanks(); i++) {
//                if (!fluidHandler.getFluidInTank(i).isEmpty() && !fluidHandler.getFluidInTank(i).getFluid().equals(Fluids.WATER))
//                    return;
//
//                int finalI = i;
//                this.fluidHandler.ifPresent(machineFluidHandler -> {
//                    int fillAmount = fluidHandler.getTankCapacity(finalI) - fluidHandler.getFluidInTank(finalI).getAmount();
//                    if (fluidHandler.fill(machineFluidHandler.drain(fillAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE) > 0)
//                        slot.putStack(fluidHandler.getContainer());
//                });
//            }
//        });
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getFuelBurnTime() {
        return fuelBurnTime;
    }

    public FluidStack getWater() {
        return waterTank.map(handler -> handler.getFluidInTank(0)).orElse(FluidStack.EMPTY);
    }

    public FluidStack getSteam() {
        return steamTank.map(handler -> handler.getFluidInTank(0)).orElse(FluidStack.EMPTY);
    }
}
