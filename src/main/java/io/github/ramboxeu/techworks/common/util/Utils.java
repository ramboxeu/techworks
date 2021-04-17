package io.github.ramboxeu.techworks.common.util;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.base.BaseEnergyStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseGasStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseLiquidStorageComponent;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Direction distributeFluid(IFluidHandler sourceTank, Direction next, TileEntity sourceTe) {
        if (!sourceTe.hasWorld()) {
            return null;
        }

        BlockPos sourcePos = sourceTe.getPos();
        World world = sourceTe.getWorld();
        List<Pair<IFluidHandler, Direction>> targets = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            TileEntity targetTe = world.getTileEntity(sourcePos.offset(direction));
            if (targetTe != null) {
                targetTe.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(h -> targets.add(Pair.of(h, direction)));
            }
        }

        if (targets.isEmpty()) {
            return null;
        }

        int totalAmount = sourceTank.getFluidInTank(0).getAmount();

        int amount = totalAmount / targets.size();
        int remainder = totalAmount - (amount * targets.size());

        Direction tempNext = null;
        boolean shouldUseTempNext = false;

        Fluid fluid = sourceTank.getFluidInTank(0).getFluid();

        for (int i = 0; i < targets.size(); i++) {
            Pair<IFluidHandler, Direction> targetPair = targets.get(i);
            Direction targetDirection = targetPair.getRight();

            int toDrain = next == targetDirection || (shouldUseTempNext && tempNext == targetDirection) ? amount + remainder : amount;
            int filled = targetPair.getLeft().fill(new FluidStack(fluid, toDrain), IFluidHandler.FluidAction.EXECUTE);
            sourceTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);

            if (filled < toDrain) {
                totalAmount = sourceTank.getFluidInTank(0).getAmount();
                amount = totalAmount / targets.size() - (i + 1);
                remainder = totalAmount - (amount * targets.size());

                if (remainder > 0) {
                    tempNext = targetDirection.rotateY();
                    shouldUseTempNext = true;
                }
            }
        }

        return next != null ? next.rotateY() : null;
    }

    public static String stringifyFluidStack(FluidStack stack) {
        try {
            return String.format("Fluid: %s, %dmb",
                    ForgeRegistries.FLUIDS.getKey(stack.getFluid()),
                    stack.getAmount());
        } catch (NullPointerException e) {
            return "Fluid: null";
        }
    }

    public static void readComponentTank(ItemStack stack, FluidTank tank) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();

            if (nbt.contains("FluidTank", Constants.NBT.TAG_COMPOUND)) {
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, nbt.getCompound("FluidTank"));

                int volume = 0;
                Item item = stack.getItem();

                if (item instanceof BaseGasStorageComponent) {
                    volume = ((BaseGasStorageComponent) item).getVolume();
                } else if (item instanceof BaseLiquidStorageComponent) {
                    volume = ((BaseLiquidStorageComponent) item).getVolume();
                }

                tank.setCapacity(volume);

                Techworks.LOGGER.debug("Read: Tag: {} | Tank: {}", nbt.getCompound("FluidTank"), tank.writeToNBT(new CompoundNBT()));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void writeComponentTank(ItemStack stack, FluidTank tank, boolean empty) {
        CompoundNBT stackTag = stack.getOrCreateTag();
        stackTag.put("FluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

        Techworks.LOGGER.debug("Write: {}", stackTag.getCompound("FluidTank"));

        if (empty) {
            tank.setCapacity(0);
            tank.setFluid(FluidStack.EMPTY);
        }
    }

    public static void readComponentBattery(ItemStack stack, EnergyBattery battery) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();

            if (nbt.contains("EnergyBattery", Constants.NBT.TAG_COMPOUND)) {
                int energy = nbt.getCompound("EnergyBattery").getInt("Energy");

                battery.setEnergy(energy);

                Item item = stack.getItem();
                if (item instanceof BaseEnergyStorageComponent) {
                    battery.setCapacity(((BaseEnergyStorageComponent) item).getCapacity());
                }
            }
        }
    }

    public static void writeComponentBattery(ItemStack stack, EnergyBattery battery, boolean empty) {
        CompoundNBT stackTag = stack.getOrCreateTag();

        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Energy", battery.getEnergyStored());

        stackTag.put("EnergyBattery", nbt);

        if (empty) {
            battery.setCapacity(0);
            battery.setEnergy(0);
        }
    }

    public static FluidStack getFluidFromNBT(CompoundNBT nbt) {
        ResourceLocation fluidId = new ResourceLocation(nbt.getString("FluidName"));
        int amount = nbt.getInt("Amount");
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        return new FluidStack(fluid, amount);
    }

    public static void writeInvToNbt(CompoundNBT nbt, String propName, IItemHandler inv) {
        INBT prop = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);

        if (prop != null) {
            nbt.put(propName, prop);
        } else {
            Techworks.LOGGER.warn("Tried to write {} as '{}', but failed", inv, propName);
        }
    }

    public static void readInvFromNbt(CompoundNBT nbt, String propName, IItemHandlerModifiable inv) {
        if (nbt.contains(propName, Constants.NBT.TAG_LIST)) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt.get(propName));
        } else {
            Techworks.LOGGER.warn("Tried to read inventory from {}, but '{}' was not found or it's not a list", nbt, propName);
        }
    }

    @Nullable
    public static <T extends HandlerConfig> T getExistingConfig(List<T> configs, HandlerData data) {
        return configs.stream().filter(config -> config.getBaseData() == data).findFirst().orElse(null);
    }

    public static float calcProgress(int value, int max) {
        if (max < 0 || value < 0) {
            return 0;
        }

        float progress = value / (float)max;
        return Math.min(Math.max(0.0f, progress), 1.0f);
    }

    public static <T> T unpack(LazyOptional<T> holder) {
        return holder.orElseThrow(() -> new RuntimeException("Present LazyOptional with no value"));
    }
}
