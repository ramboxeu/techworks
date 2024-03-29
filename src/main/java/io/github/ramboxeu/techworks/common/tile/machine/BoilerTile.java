package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.capability.HandlerStorage;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.fluid.handler.GasTank;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.heat.HeaterConfigurationStore;
import io.github.ramboxeu.techworks.common.heat.IConfiguringHeater;
import io.github.ramboxeu.techworks.common.heat.IHeater;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTags;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.FluidUtils;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.machineio.MachineIO;
import io.github.ramboxeu.techworks.common.util.machineio.data.GasHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

public class BoilerTile extends BaseMachineTile {
    private static final int HEATING_RATE = 10;
    private static final int COOLING_RATE = 1;

    private final LiquidTank waterTank;
    private final LiquidHandlerData waterTankData;
    private final GasTank steamTank;
    private final GasHandlerData steamTankData;
    private final Collection<SteamEngineTile> steamEngines;
    private final HeaterConfigurationStore heaterConfig;
    private IHeater heater;

    private int fuelBurnTime = 0;
    private boolean isWorking;
    private boolean isBurning;

    private int temperature;

    public BoilerTile() {
        super(TechworksTiles.BOILER.get());

        waterTank = new LiquidTank() {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isIn(FluidTags.WATER);
            }
        };
        waterTankData = machineIO.getHandlerData(waterTank, MachineIO.INPUT);

        steamTank = new GasTank(){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isIn(TechworksFluidTags.STEAM);
            }
        };
        steamTankData = machineIO.getHandlerData(steamTank, MachineIO.OUTPUT);

        heaterConfig = new HeaterConfigurationStore(machineIO);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.LIQUID_STORAGE.get(), waterTank)
                .component(TechworksComponents.GAS_STORAGE.get(), steamTank)
                .component(TechworksComponents.HEATING.get(), (component, stack) -> {
                    heater = component.getHeaterType().createHeater();

                    handlers.disable(heaterConfig.getHandlerStorageFlags());

                    heaterConfig.clear();
                    if (heater instanceof IConfiguringHeater)
                        ((IConfiguringHeater) heater).configure(heaterConfig);

                    handlers.enable(heaterConfig.getHandlerStorageFlags());
                })
                .build();
        handlers.enable(HandlerStorage.LIQUID | HandlerStorage.GAS);

        steamEngines = new ArrayList<>(4);
    }

    @Override
    protected void workTick() {
        heater.tick();

        int receivedHeat = heater.extractHeat(false);

        if (receivedHeat > 0) {
            if (temperature > receivedHeat) {
                temperature = Math.max(receivedHeat, temperature - COOLING_RATE);
            } else {
                temperature = Math.min(receivedHeat, temperature + HEATING_RATE);
            }

            int steam = temperature * 4;
            if (waterTank.getFluidAmount() >= 50 && steamTank.getFluidAmount() + steam <= steamTank.getCapacity()) {
                waterTank.drain(50, IFluidHandler.FluidAction.EXECUTE);

                if (!steamEngines.isEmpty()) {
                    int steamAmount = steam / steamEngines.size();
                    int maxPower = steam / 4;
                    int maxDrainAmount = steamTank.getFluidAmount() / steamEngines.size();

                    if (maxDrainAmount > 0) {
                        int amountDrained = 0;

                        for (int i = 0, size = steamEngines.size(); i < size; i++) {
                            amountDrained += steamTank.drain(maxDrainAmount, IFluidHandler.FluidAction.EXECUTE).getAmount();
                        }

                        steamAmount += amountDrained / steamEngines.size();
                        maxPower += amountDrained / 4;
                    }

                    for (SteamEngineTile engine : steamEngines) {
                        engine.receiveSteam(steamAmount, maxPower);
                    }
                } else {
                    steamTank.fill(new FluidStack(TechworksFluids.STEAM.get(), steam), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        } else {
            temperature = Math.max(0, temperature - COOLING_RATE);

            if (steamTank.getFluidAmount() > 0 && !steamEngines.isEmpty()) {
                int maxDrainAmount = steamTank.getFluidAmount() / steamEngines.size();

                if (maxDrainAmount > 0) {
                    int amountDrained = 0;

                    for (int i = 0, size = steamEngines.size(); i < size; i++) {
                        amountDrained += steamTank.drain(maxDrainAmount, IFluidHandler.FluidAction.EXECUTE).getAmount();
                    }

                    int maxPower = amountDrained / 4;
                    int steamAmount = amountDrained / steamEngines.size();

                    for (SteamEngineTile engine : steamEngines) {
                        engine.receiveSteam(steamAmount, maxPower);
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType onRightClick(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return FluidUtils.onHandlerInteraction(player, hand, waterTank) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("FuelBurnTime", fuelBurnTime);
        tag.putBoolean("IsWorking", isWorking);
        tag.putBoolean("IsBurning", isBurning);
        tag.put("WaterTank", waterTank.serializeNBT());
        tag.put("SteamTank", steamTank.serializeNBT());

        if (heater instanceof INBTSerializable) {
            tag.put("Heater", ((INBTSerializable<?>) heater).serializeNBT());
        }

        return super.write(tag);
    }


    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);

        fuelBurnTime = tag.getInt("FuelBurnTime");
        isWorking = tag.getBoolean("IsWorking");
        isBurning = tag.getBoolean("IsBurning");
        waterTank.deserializeNBT(tag.getCompound("WaterTank"));
        steamTank.deserializeNBT(tag.getCompound("SteamTank"));

        if (heater instanceof INBTSerializable) {
            ((INBTSerializable<CompoundNBT>) heater).deserializeNBT(tag.getCompound("Heater"));
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.BOILER.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BoilerContainer(id, playerInventory, this);
    }

    @Override
    public void remove() {
        super.remove();
        steamEngines.forEach(SteamEngineTile::unlinkBoiler);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        steamEngines.forEach(SteamEngineTile::unlinkBoiler);
    }

    @Override
    public Collection<ItemStack> getDrops() {
        Collection<ItemStack> drops = super.getDrops();
        drops.addAll(heaterConfig.getDrops());
        return drops;
    }

    public LiquidHandlerData getWaterTankData() {
        return waterTankData;
    }

    public GasHandlerData getSteamTankData() {
        return steamTankData;
    }

    public IHeater getHeater() {
        return heater;
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public boolean configure(PlayerEntity player, ItemStack wrenchStack, World world, BlockPos pos, Direction face, Vector3d hitVec) {
        CompoundNBT wrenchTag = wrenchStack.getOrCreateChildTag("Wrench");

        if (wrenchTag.contains("BoilerChain", Constants.NBT.TAG_COMPOUND)) {
            BlockPos startPos = NBTUtils.getBlockPos(wrenchTag, "BoilerChain");

            if (startPos.equals(pos)) {
                wrenchTag.remove("BoilerChain");
                player.sendStatusMessage(TranslationKeys.ENGINE_LINKING_CANCELLED.text(pos.getX(), pos.getY(), pos.getZ()), true);
            }
        } else {
            NBTUtils.putBlockPos(wrenchTag, "BoilerChain", pos);
            player.sendStatusMessage(TranslationKeys.LINKING_ENGINES.text(pos.getX(), pos.getY(), pos.getZ()), true);
        }

        return true;
    }

    public boolean linkEngine(SteamEngineTile tile) {
        if (steamEngines.size() < 4) {
            steamEngines.add(tile);
            return true;
        }

        return false;
    }

    public void unlinkEngine(SteamEngineTile tile) {
        steamEngines.remove(tile);
    }

    public Collection<SteamEngineTile> getLikedEngines() {
        return steamEngines;
    }
}
