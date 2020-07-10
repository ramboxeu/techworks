package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.api.gas.CapabilityGas;
import io.github.ramboxeu.techworks.api.gas.GasHandler;
import io.github.ramboxeu.techworks.api.gas.IGasHandler;
import io.github.ramboxeu.techworks.client.container.BoilerContainer;
import io.github.ramboxeu.techworks.common.component.IComponentsContainerProvider;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.registration.Registration;
import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.PredicateUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BoilerTile extends BaseMachineTile implements IComponentsContainerProvider {
    private int cookTime = 0;
    private int cachedBurnTime = 0;
    private int counter = 0;
    private boolean isBurning;

    private final IItemHandler fuelInventory;
    private final IFluidHandler waterTank;
    private final IGasHandler steamTank;
    private final ComponentStackHandler components;

    public BoilerTile() {
        super(Registration.BOILER_TILE.get());

        components = ComponentStackHandler.withSize(3);

        fuelInventory = new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return PredicateUtils.isFuel(stack);
            }
        };

        waterTank = new FluidTank(10000, fluid -> fluid.getFluid().isIn(FluidTags.WATER));

        steamTank = new GasHandler(Registration.STEAM_GAS.get(), 10000, 10000);


//        this.capabilities[1] = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
//        this.optionals[1] = LazyOptional.of(() -> fuelInventory);
    }

    @Override
    public void tick() {
        int burnTime = ForgeHooks.getBurnTime(fuelInventory.getStackInSlot(0));

        int fluidAmount = waterTank.getFluidInTank(0).getAmount();
        int gasAmount = steamTank.getAmountStored();

        if (!isBurning && burnTime > 0 && fluidAmount >= 400 && gasAmount <= steamTank.getMaxStorage() - 400 &&
                !fuelInventory.extractItem(0, 1, true).isEmpty()) {
            fuelInventory.extractItem(0, 1, false);
            isBurning = true;
            cachedBurnTime = burnTime;
            this.getBlockState().with(TechworksBlockStateProperties.RUNNING, true);
        }

        if (isBurning) {
            if (cookTime == cachedBurnTime) {
                cookTime = 0;
                cachedBurnTime = 0;
                isBurning = false;
                if (burnTime == 0) {
                    counter = 0;
                }
                this.getBlockState().with(TechworksBlockStateProperties.RUNNING, false);
            } else {
                cookTime++;
            }

            if (counter == 200) {
                if (steamTank.insertGas(Registration.STEAM_GAS.get(), 400, true) == 400
                        && waterTank.drain(400, IFluidHandler.FluidAction.SIMULATE).getAmount() == 400)
                {
                    steamTank.insertGas(Registration.STEAM_GAS.get(), 400, false);
                    waterTank.drain(400, IFluidHandler.FluidAction.EXECUTE);
                }
                counter = 0;
            } else {
                counter++;
            }
        } else {
            this.getBlockState().with(TechworksBlockStateProperties.RUNNING, false);
        }

        // Maybe a nice util for that?
        List<IGasHandler> handlers = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            TileEntity te = world.getTileEntity(pos.offset(direction));
            if (te != null) {
                te.getCapability(CapabilityGas.GAS, direction.getOpposite()).ifPresent(handlers::add);
            }
        }

        for (int i = handlers.size(); i > 0; i--) {
            handlers.get(i - 1).insertGas(Registration.STEAM_GAS.get(), steamTank.extractGas(Registration.STEAM_GAS.get(), steamTank.getAmountStored() / i, false), false);
        }
    }

    @Override
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        AtomicReference<ActionResultType> resultType = new AtomicReference<>(ActionResultType.PASS);

        if (!worldIn.isRemote) {
            ItemStack handStack = player.getHeldItem(handIn);

            handStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler -> {
                int maxDrain = waterTank.getTankCapacity(0) - waterTank.getFluidInTank(0).getAmount();

                if (fluidHandler.drain(maxDrain, IFluidHandler.FluidAction.SIMULATE).getFluid().isIn(FluidTags.WATER)) {
                    waterTank.fill(fluidHandler.drain(maxDrain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    player.setHeldItem(handIn, fluidHandler.getContainer());
                    resultType.set(ActionResultType.SUCCESS);
                }
            });
        }

        return resultType.get();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("counter", counter);
        compound.putInt("cookTime", cookTime);
        compound.putInt("cachedBurnTime", cachedBurnTime);
        compound.putBoolean("isBurning", isBurning);

        compound.put("FuelInv", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(fuelInventory, null));
        compound.put("WaterTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(waterTank, null));
        compound.put("SteamTank", CapabilityGas.GAS.writeNBT(steamTank, null));
        compound.put("ComponentInv", components.serializeNBT());

        return super.write(compound);
    }


    @Override
    // a. k. a read
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        counter = compound.getInt("counter");
        cookTime = compound.getInt("cookTime");
        cachedBurnTime = compound.getInt("cachedBurnTime");
        isBurning = compound.getBoolean("isBurning");

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(fuelInventory, null, compound.get("FuelInv"));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(waterTank, null, compound.get("WaterTank"));
        CapabilityGas.GAS.readNBT(steamTank, null, compound.get("SteamTank"));
        components.deserializeNBT(compound.getCompound("ComponentInv"));

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
        return new StringTextComponent("Boiler's Components");
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

    public int getCookTime() {
        return cookTime;
    }

    public int getBurnTime() {
        return cachedBurnTime;
    }
}
