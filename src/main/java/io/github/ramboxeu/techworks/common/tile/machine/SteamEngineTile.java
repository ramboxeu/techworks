package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.base.BaseEnergyStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseGasStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseSteamTurbineComponent;
import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.capability.impl.EnergyBattery;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTags;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.Optional;

import static io.github.ramboxeu.techworks.api.component.ComponentStackHandler.Builder;
import static io.github.ramboxeu.techworks.api.component.ComponentStackHandler.Slot;

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

    private Optional<BaseSteamTurbineComponent> steamTurbine = Optional.empty();

    private boolean isSteamTankPresent = false;
    private boolean isEnergyStoragePresent = false;

    private boolean isWorking = false;
    private int workTime = 1;

    public SteamEngineTile() {
        super(TechworksTiles.STEAM_ENGINE.getTileType(), new Builder(3)
                        .slot(0, new Slot()
                                .predicate(stack -> stack.getItem() instanceof BaseGasStorageComponent)
                                .texture(new ResourceLocation(Techworks.MOD_ID, "textures/gui/slot/gas_storage_component_overlay.png"))
                        )
                        .slot(1, new Slot()
                                .predicate(stack -> stack.getItem() instanceof BaseSteamTurbineComponent)
                        )
                        .slot(2, new Slot()
                                .predicate(stack -> stack.getItem() instanceof BaseEnergyStorageComponent)
                        )
        );

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
        boolean flag = true;

        if (!isSteamTankPresent) {
            flag = false;
        }

        if (!steamTurbine.isPresent()) {
            flag = false;
        }

        if (!isEnergyStoragePresent) {
            flag = false;
        }

        if (flag) {
            BaseSteamTurbineComponent steamTurbine = this.steamTurbine.get();

            if (!isWorking) {
                if (canWork(steamTank, energyStorage, steamTurbine)) {
                    steamTank.drain(steamTurbine.getSteam(), IFluidHandler.FluidAction.EXECUTE);
                    isWorking = true;
                    workTime = 1;
                }
            }

            if (isWorking) {
                // works (n - 1), stops (n), consumes and works again (1)
                // gives 1 tick delay each work cycle
                if (workTime == steamTurbine.getWorkTime()) {
                    isWorking = false;
                    workTime = 1;
                } else {
                    energyStorage.receiveEnergy(steamTurbine.getEnergy(), false);
                    Techworks.LOGGER.debug("Released energy");
                    workTime++;
                }
            }

            Techworks.LOGGER.debug("WorkTime: {} | IsWorking: {}", workTime, isWorking);
        }
    }

    private boolean canWork(IFluidHandler steam, IEnergyStorage energy, BaseSteamTurbineComponent turbine) {
        int totalEnergy = turbine.getEnergy() * turbine.getWorkTime();
        FluidStack steamStack = steam.drain(turbine.getSteam(), IFluidHandler.FluidAction.SIMULATE);

        Techworks.LOGGER.debug(Utils.stringifyFluidStack(steamStack));

        return (steamStack.getAmount() == turbine.getSteam() &&
                energy.receiveEnergy(totalEnergy, true) == totalEnergy);
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
    protected void refreshComponents(ItemStack stack, boolean input) {
        Item item = stack.getItem();

        if (item instanceof BaseGasStorageComponent) {
            if (input) {
                Utils.readComponentTank(stack, steamTank);
                isSteamTankPresent = true;
            } else {
                Utils.writeComponentTank(stack, steamTank, true);
                isSteamTankPresent = false;
            }
        }

        if (item instanceof BaseEnergyStorageComponent) {
            if (input) {
                Utils.readComponentBattery(stack, energyStorage);
                isEnergyStoragePresent = true;
            } else {
                Utils.writeComponentBattery(stack, energyStorage, true);
                isEnergyStoragePresent = false;
            }
        }

        if (item instanceof BaseSteamTurbineComponent) {
            steamTurbine = input ? Optional.of((BaseSteamTurbineComponent) item) : Optional.empty();
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (isSteamTankPresent) {
            Utils.writeComponentTank(components.getStackInSlot(0), steamTank, false);
        }

        if (isEnergyStoragePresent) {
            Utils.writeComponentBattery(components.getStackInSlot(2), energyStorage, false);
        }
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
