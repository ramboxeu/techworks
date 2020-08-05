package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.base.BaseEnergyStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseGasStorageComponent;
import io.github.ramboxeu.techworks.api.component.base.BaseSteamTurbineComponent;
import io.github.ramboxeu.techworks.client.container.machine.SteamEngineContainer;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTags;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.capability.EmptyEnergyHandler;
import io.github.ramboxeu.techworks.common.util.capability.EmptyTankItem;
import io.github.ramboxeu.techworks.common.util.capability.EnergyWrapper;
import io.github.ramboxeu.techworks.common.util.capability.FluidWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

import javax.annotation.Nullable;

import java.util.Optional;

import static io.github.ramboxeu.techworks.api.component.ComponentStackHandler.*;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SteamEngineTile extends BaseMachineTile {
    private LazyOptional<IFluidHandlerItem> steamTank = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.empty();
    private Optional<BaseSteamTurbineComponent> steamTurbine = Optional.empty();

    private final FluidWrapper steamWrapper = new FluidWrapper(steamTank, stack -> stack.getFluid().isIn(TechworksFluidTags.STEAM));
    private final EnergyWrapper energyWrapper = new EnergyWrapper(energyStorage);

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

        machineIO = MachineIO.create(
                MachineIO.PortConfig.create(MachinePort.Type.GAS, steamWrapper),
                MachineIO.PortConfig.create(MachinePort.Type.ENERGY, energyWrapper)
        );
    }

    @Override
    protected void serverTick() {
        boolean flag = true;

        if (!steamTank.isPresent()) {
            flag = false;
        }

        if (!steamTurbine.isPresent()) {
            flag = false;
        }

        if (!energyStorage.isPresent()) {
            flag = false;
        }

        if (flag) {
            BaseSteamTurbineComponent steamTurbine = this.steamTurbine.get();

            if (!isWorking) {
                IFluidHandler steamTank = this.steamTank.orElse(EmptyTankItem.INSTANCE);
                IEnergyStorage energyStorage = this.energyStorage.orElse(EmptyEnergyHandler.INSTANCE);

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
                    energyStorage.orElse(EmptyEnergyHandler.INSTANCE).receiveEnergy(steamTurbine.getEnergy(), false);
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
    protected void markComponentsDirty(boolean forced) {
        if ((world != null && !world.isRemote) || forced) {
            steamTank = components.getStackInSlot(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
            energyStorage = components.getStackInSlot(2).getCapability(CapabilityEnergy.ENERGY);

            steamWrapper.setHandler(steamTank);
            energyWrapper.setHandler(energyStorage);

            Item turbineItem = components.getStackInSlot(1).getItem();
            steamTurbine = turbineItem instanceof BaseSteamTurbineComponent ? Optional.of((BaseSteamTurbineComponent) turbineItem) : Optional.empty();
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
        return new SteamEngineContainer(id, inventory, this);
    }
}
