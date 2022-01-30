package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.LiquidPumpContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.machineio.data.EnergyHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class LiquidPumpTile extends BaseMachineTile {
    private final LiquidTank tank;
    private final LiquidHandlerData tankData;
    private final EnergyBattery battery;
    private final EnergyHandlerData batteryData;
    private final Queue<BlockPos> queue;
    private BlockPos lastPos;
    private int extractedEnergy;
    private boolean shouldCheck;
    private boolean isWorking;

    public LiquidPumpTile() {
        super(TechworksTiles.LIQUID_PUMP.get());

        tank = new LiquidTank() {
            @Override
            protected void onContentsChanged() {
                shouldCheck = true;
            }
        };
        tankData = machineIO.getHandlerData(tank);

        battery = new EnergyBattery() {
            @Override
            protected void onContentsChanged() {
                shouldCheck = true;
            }
        };
        batteryData = machineIO.getHandlerData(battery);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.LIQUID_STORAGE.get(), tank)
                .component(TechworksComponents.ENERGY_STORAGE.get(), battery)
                .build();

        queue = new ArrayDeque<>();
    }

    @Override
    protected void workTick() {
        if (shouldCheck) {
            if (battery.getEnergyStored() >= 2000 && tank.getFluidAmount() < tank.getCapacity()) {
                isWorking = true;
            } else {
                isWorking = false;
                extractedEnergy = 0;
            }

            setWorkingState(isWorking);
            shouldCheck = false;
        }

        if (isWorking) {
            if (extractedEnergy < 2000) {
                extractedEnergy += battery.extractEnergy(25, false);
            }

            if (extractedEnergy == 2000) {
                if (tank.getFluidAmount() == tank.getCapacity())
                    return;

                extractedEnergy = 0;
                shouldCheck = true;
                isWorking = false;

                if (lastPos == null) {
                    lastPos = pos.down();
                }

                FluidState state = world.getFluidState(lastPos);

                if (isFluidStateValid(state)) {
                    if (world.setBlockState(lastPos, Blocks.AIR.getDefaultState())) {
                        tank.fill(new FluidStack(state.getFluid(), FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE, true);
                    }
                } else {
                    if (queue.isEmpty()) {
                        findSource();
                    }

                    if (!queue.isEmpty()) {
                        lastPos = queue.poll();

                        FluidState nextState = world.getFluidState(lastPos);

                        if (isFluidStateValid(nextState)) {
                            if (world.setBlockState(lastPos, Blocks.AIR.getDefaultState())) {
                                tank.fill(new FluidStack(nextState.getFluid(), FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE, true);

                                BlockPos.Mutable current = lastPos.toMutable().move(-1, 0, -1);
                                for (int x = 0; x < 3; x++) {
                                    for (int z = 0; z < 3; z++) {
                                        if (!lastPos.equals(current) && current.withinDistance(pos, 16)) {
                                            FluidState neighbourState = world.getFluidState(current);

                                            if (isFluidStateValid(neighbourState) && !queue.contains(current)) {
                                                queue.offer(current.toImmutable());
                                            }
                                        }

                                        current.move(0, 0, 1);
                                    }

                                    current.move(1, 0, -3);
                                }

                                current.move(-2, -1, 1);
                                if (current.withinDistance(pos, 16)) {
                                    FluidState neighbourState = world.getFluidState(current);

                                    if (isFluidStateValid(neighbourState) && !queue.contains(current)) {
                                        queue.offer(current.toImmutable());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("Tank", tank.serializeNBT());
        tag.putInt("ExtractedEnergy", extractedEnergy);
        tag.putBoolean("IsWorking", isWorking);
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        tank.deserializeNBT(tag.getCompound("Tank"));
        extractedEnergy = tag.getInt("ExtractedEnergy");
        isWorking = tag.getBoolean("IsWorking");
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.LIQUID_PUMP.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player) {
        return new LiquidPumpContainer(id, playerInv, this);
    }

    private void findSource() {
        Queue<BlockPos.Mutable> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.offer(pos.toMutable().move(0, -1, 0));

        while (!queue.isEmpty()) {
            BlockPos.Mutable current = queue.poll();
            visited.add(current.toImmutable());

            FluidState fluid = world.getFluidState(current);
            if (isFluidStateValid(fluid)) {
                this.queue.offer(current);
                return;
            }

            BlockState block = world.getBlockState(current);
            if (fluid.getFluid() == Fluids.EMPTY && !block.isAir()) {
                continue;
            }

            BlockPos parent = current.toImmutable();
            current.move(-1, 0, -1);
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    if (!current.equals(parent) && current.withinDistance(pos, 16) && !visited.contains(current) && !queue.contains(current)) {
                        queue.offer(current.toMutable());
                    }

                    current.move(0, 0, 1);
                }

                current.move(1, 0, -3);
            }

            current.move(-2, -1, 1);
            if (current.withinDistance(pos, 16) && !queue.contains(current)) {
                queue.offer(current.toMutable());
            }
        }
    }

    private boolean isFluidStateValid(FluidState state) {
        Fluid fluid = state.getFluid();
        FluidStack stored = tank.getFluid();

        return fluid != Fluids.EMPTY && (fluid.isSource(state) && !fluid.getAttributes().isGaseous() && stored.isEmpty() || stored.getFluid() == fluid);
    }

    public LiquidHandlerData getTankData() {
        return tankData;
    }

    public EnergyHandlerData getBatteryData() {
        return batteryData;
    }
}
