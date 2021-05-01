package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.energy.EnergyBattery;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTags;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SteamEngineTile extends BaseMachineTile {
    private FluidTank steamTank = new FluidTank(0, stack -> stack.getFluid().isIn(TechworksFluidTags.STEAM)){
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private EnergyBattery energyStorage = new EnergyBattery(0, 100, 100) {
        @Override
        protected void onContentsChanged() {
            markDirty(); // This is run every tick. Is it stupid? probably...
        }
    };

    private boolean isSteamTankPresent = false;
    private boolean isEnergyStoragePresent = false;

    private boolean isWorking = false;
    private int workTime = 1;

    public SteamEngineTile() {
        super(TechworksTiles.STEAM_ENGINE.get());

        // FIXME: 10/10/2020 
//        machineIO = MachineIO.create(
//                MachineIO.PortConfig.create(MachinePort.Type.GAS, steamTank),
//                MachineIO.PortConfig.create(MachinePort.Type.ENERGY, energyStorage)
//        );
    }

    @Override
    protected void onFirstTick() {
        super.onFirstTick();

//        machineIO.setFacing(getFacing());
    }

    @Override
    protected void serverTick() {
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
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("WorkTime", workTime);
        compound.putBoolean("IsWorking", isWorking);

        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        workTime = compound.getInt("WorkTime");
        isWorking = compound.getBoolean("IsWorking");

        super.read(state, compound);
    }

    @Override
    protected ITextComponent getComponentsGuiName() {
        return new TranslationTextComponent("container.techworks.steam_engine_components");
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
}
