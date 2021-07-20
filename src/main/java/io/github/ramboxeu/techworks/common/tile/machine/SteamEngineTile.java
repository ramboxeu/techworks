package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class SteamEngineTile extends BaseMachineTile {
    private EnergyBattery energyStorage = new EnergyBattery(0, 100, 100) {
        @Override
        protected void onContentsChanged() {
            markDirty(); // This is run every tick. Is it stupid? probably...
        }
    };

    private BoilerTile boiler;
    private Direction boilerSide;
    private FluidStack steam;
    private boolean isLinked;
    private boolean isWorking = false;
    private int maxPower;

    public SteamEngineTile() {
        super(TechworksTiles.STEAM_ENGINE.get());

        components = new ComponentStorage.Builder().build();
        steam = FluidStack.EMPTY;
    }

    @Override
    protected void onFirstTick() {
        if (!isLinked) {
            for (Direction side : Direction.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(side));

                if (tile instanceof BoilerTile) {
                    link((BoilerTile) tile, side, 3);
                }

                if (tile instanceof SteamEngineTile) {
                    for (int i = 2; i <= 4; i++) {
                        TileEntity next = world.getTileEntity(pos.offset(side, i));

                        if (next instanceof SteamEngineTile) {
                            continue;
                        }

                        if (next instanceof BoilerTile) {
                            link((BoilerTile) next, side, 4 - i);
                            break;
                        }

                        break;
                    }
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();

        if (isLinked) {
            boiler.unlinkEngine(this, true);
            boiler = null;
            boilerSide = null;
            isLinked = false;
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if (isLinked) {
            boiler.unlinkEngine(this, false);
            boiler = null;
            boilerSide = null;
            isLinked = false;
        }
    }

    @Override
    protected void serverTick() {
        // process
//        Techworks.LOGGER.debug("Received steam: {}", steam.getAmount());
        steam = FluidStack.EMPTY;
        maxPower = 0;
//        boolean flag = true;
//
//        if (!isSteamTankPresent) {
//            flag = false;
//        }
//
//        if (!steamTurbine.isPresent()) {
//            flag = false;
//        }
//
//        if (!isEnergyStoragePresent) {
//            flag = false;
//        }
//
//        if (flag) {
//            BaseSteamTurbineComponent steamTurbine = this.steamTurbine.get();
//
//            if (!isWorking) {
//                if (canWork(steamTank, energyStorage, steamTurbine)) {
//                    steamTank.drain(steamTurbine.getSteam(), IFluidHandler.FluidAction.EXECUTE);
//                    isWorking = true;
//                    workTime = 1;
//                }
//            }
//
//            if (isWorking) {
//                // works (n - 1), stops (n), consumes and works again (1)
//                // gives 1 tick delay each work cycle
//                if (workTime == steamTurbine.getWorkTime()) {
//                    isWorking = false;
//                    workTime = 1;
//                } else {
//                    energyStorage.receiveEnergy(steamTurbine.getEnergy(), false);
//                    Techworks.LOGGER.debug("Released energy");
//                    workTime++;
//                }
//            }
//
//            Techworks.LOGGER.debug("WorkTime: {} | IsWorking: {}", workTime, isWorking);
//        }
    }

    @Override
    protected void buildComponentStorage(ComponentStorage.Builder builder) {

    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putBoolean("IsWorking", isWorking);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        isWorking = tag.getBoolean("IsWorking");

        super.read(state, tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.techworks.steam_engine");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity) {
        return new SteamEngineContainer(id, inventory, this, machineIO.createDataMap());
    }

    public void receiveSteam(int amount, int maxPower) {
        this.maxPower = maxPower;
        steam = new FluidStack(TechworksFluids.STEAM.get(), amount);
    }

    public boolean validateLink() {
        for (int i = 1; i <= 4; i++) {
            TileEntity tile = world.getTileEntity(pos.offset(boilerSide, i));

            if (tile instanceof SteamEngineTile) continue;
            if (tile instanceof BoilerTile) return true;

            boilerSide = null;
            boiler = null;
            isLinked = false;
            return false;
        }

        return false;
    }

    public void unlink() {
        boiler = null;
        boilerSide = null;
        isLinked = false;
    }

    public void link(BoilerTile boiler, Direction side) {
        if (!isLinked) {
            this.boiler = boiler.linkEngine(this, side);
            boilerSide = side;
            isLinked = this.boiler != null;
        }
    }

    private void link(BoilerTile boiler, Direction side, int engines) {
        this.boiler = boiler.linkEngine(this, side);
        boilerSide = side;
        isLinked = this.boiler != null;

        if (engines > 0) {
            for (int i = 1; i <= engines; i++) {
                TileEntity tile = world.getTileEntity(pos.offset(side.getOpposite(), i));

                if (tile instanceof SteamEngineTile) {
                    ((SteamEngineTile) tile).link(boiler, side);
                } else {
                    break;
                }
            }
        }
    }
}
