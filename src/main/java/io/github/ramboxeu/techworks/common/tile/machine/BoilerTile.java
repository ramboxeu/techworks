package io.github.ramboxeu.techworks.common.tile.machine;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.machine.BoilerContainer;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.fluid.handler.GasTank;
import io.github.ramboxeu.techworks.common.fluid.handler.LiquidTank;
import io.github.ramboxeu.techworks.common.heat.IHeater;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.registration.TechworksFluids;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.tag.TechworksFluidTags;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.machineio.data.GasHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.LiquidHandlerData;
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
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
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
        waterTankData = machineIO.getHandlerData(waterTank);

        steamTank = new GasTank(){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isIn(TechworksFluidTags.STEAM);
            }
        };
        steamTankData = machineIO.getHandlerData(steamTank);

        components = new ComponentStorage.Builder()
                .component(TechworksComponents.LIQUID_STORAGE.get(), waterTank)
                .component(TechworksComponents.GAS_STORAGE.get(), steamTank)
                .component(TechworksComponents.HEATING.get(), (component, stack) -> heater = component.getHeaterType().createHeater())
                .build();

        steamEngines = new ArrayList<>(4);
    }

    @Override
    protected void onFirstTick() {
        if (steamEngines.isEmpty()) {
            int chainLength = 0;
            int maxChainLength = 0;
            Direction chainSide = null;
            Direction maxLengthSide = null;

            for (Direction side : Direction.values()) {
                boolean maxLength = true;

                for (int i = 1; i <= 4; i++) {
                    TileEntity tile = world.getTileEntity(pos.offset(side, i));

                    if (!(tile instanceof SteamEngineTile) && i > 1) {
                        chainLength = i - 1;
                        chainSide = side;
                        maxLength = false;
                        break;
                    }
                }

                if (maxLength) {
                    chainSide = side;
                    chainLength = 4;
                }

                if (maxChainLength < chainLength) {
                    maxChainLength = chainLength;
                    maxLengthSide = chainSide;
                }
            }

            for (int i = 1; i <= maxChainLength; i++) {
                SteamEngineTile engine = (SteamEngineTile) world.getTileEntity(pos.offset(maxLengthSide, i));
                engine.link(this, maxLengthSide.getOpposite());
            }
        }
    }

    @Override
    protected void serverTick() {
        heater.tick();

        int receivedHeat = heater.extractHeat();

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
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            ItemStack handStack = player.getHeldItem(handIn);

            LazyOptional<IFluidHandlerItem> bucket = handStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

            if (bucket.isPresent()) {
                IFluidHandlerItem bucketTank = Utils.unpack(bucket);
                int maxDrain = waterTank.getCapacity() - waterTank.getFluidAmount();

                if (maxDrain >= 1000) {
                    if (bucketTank.drain(maxDrain, IFluidHandler.FluidAction.SIMULATE).getFluid().isIn(FluidTags.WATER)) {
                        waterTank.fill(bucketTank.drain(maxDrain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE, true);
                        player.setHeldItem(handIn, bucketTank.getContainer());
                        markDirty();

                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    protected void buildComponentStorage(ComponentStorage.Builder builder) {

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
        fuelBurnTime = tag.getInt("FuelBurnTime");
        isWorking = tag.getBoolean("IsWorking");
        isBurning = tag.getBoolean("IsBurning");
        waterTank.deserializeNBT(tag.getCompound("WaterTank"));
        steamTank.deserializeNBT(tag.getCompound("SteamTank"));

        if (heater instanceof INBTSerializable) {
            ((INBTSerializable<CompoundNBT>) heater).deserializeNBT(tag.getCompound("Heater"));
        }

        super.read(state, tag);
    }

    @Override
    public ITextComponent getDisplayName() {
        return TranslationKeys.BOILER.text();
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BoilerContainer(id, playerInventory, this, machineIO.createDataMap());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction face) {
        if (heater instanceof ICapabilityProvider) {
            LazyOptional<T> heaterCap = ((ICapabilityProvider) heater).getCapability(cap, face);

            if (heaterCap.isPresent())
                return heaterCap;
        }

        return super.getCapability(cap, face);
    }

    @Override
    public void remove() {
        super.remove();
        steamEngines.forEach(SteamEngineTile::unlink);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        steamEngines.forEach(SteamEngineTile::unlink);
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

    public BoilerTile linkEngine(SteamEngineTile tile, Direction side) {
        if (steamEngines.size() < 4) {
            steamEngines.add(tile);
            Techworks.LOGGER.debug("Linked engine: {} @ {}", tile, tile.getPos());
            return this;
        }

        Techworks.LOGGER.debug("Linked engine cap reached: {} @ {}", tile, tile.getPos());
        return null;
    }

    public void unlinkEngine(SteamEngineTile tile, boolean validateOtherLinks) {
        steamEngines.remove(tile);

        if (validateOtherLinks) {
            steamEngines.removeIf(engine -> !engine.validateLink());
        }

        Techworks.LOGGER.debug("Unlinked engine: {} @ {}", tile, tile.getPos());
    }
}
