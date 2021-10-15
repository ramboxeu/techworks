package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.client.container.DevBlockContainer;
import io.github.ramboxeu.techworks.common.energy.DevEnergyStorage;
import io.github.ramboxeu.techworks.common.fluid.handler.DevFluidHandler;
import io.github.ramboxeu.techworks.common.item.handler.DevItemHandler;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.registration.TechworksTiles;
import io.github.ramboxeu.techworks.common.util.Predicates;
import io.github.ramboxeu.techworks.common.util.Side;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

public class DevBlockTile extends BaseTechworksTile implements INamedContainerProvider {
    private static final Marker DEVTILE = MarkerManager.getMarker("DEVTILE");

    private final List<String> logs = new ArrayList<>();

    private final DevEnergyStorage energy;
    private final LazyOptional<IEnergyStorage> energyHolder;
    private ActiveSignal energyActiveSignal;
    private EnumSet<Side> energySides;
    private int energyPerTick;

    private final DevFluidHandler liquid;
    private final LazyOptional<IFluidHandler> liquidHolder;
    private ActiveSignal liquidActiveSignal;
    private EnumSet<Side> liquidSides;
    private int liquidPerTick;

    private final DevFluidHandler gas;
    private final LazyOptional<IFluidHandler> gasHolder;
    private ActiveSignal gasActiveSignal;
    private EnumSet<Side> gasSides;
    private int gasPerTick;

    private final DevItemHandler inv;
    private final LazyOptional<IItemHandler> invHolder;
    private ActiveSignal invActiveSignal;
    private EnumSet<Side> invSides;

    public DevBlockTile() {
        super(TechworksTiles.DEV_BLOCK.get());
        energy = new DevEnergyStorage(this);
        energyHolder = LazyOptional.of(() -> energy);
        energyActiveSignal = ActiveSignal.NEVER;
        energySides = EnumSet.noneOf(Side.class);

        liquid = new DevFluidHandler(this, Predicates::isLiquid);
        liquidHolder = LazyOptional.of(() -> liquid);
        liquidActiveSignal = ActiveSignal.NEVER;
        liquidSides = EnumSet.noneOf(Side.class);

        gas = new DevFluidHandler(this, Predicates::isGas);
        gasHolder = LazyOptional.of(() -> gas);
        gasActiveSignal = ActiveSignal.NEVER;
        gasSides = EnumSet.noneOf(Side.class);

        inv = new DevItemHandler(this);
        invHolder = LazyOptional.of(() -> inv);
        invActiveSignal = ActiveSignal.NEVER;
        invSides = EnumSet.noneOf(Side.class);
    }

    public void createLog(String message) {
        String log = (world.getGameTime() % 100) + (world.isRemote ? "C" : "S") + " " + message;
        logs.add(log);
//        Techworks.LOGGER.debug(DEVTILE, log);

        if (world != null && !world.isRemote) {
//            Techworks.LOGGER.debug("Syncing server logs!");
            TechworksPacketHandler.syncDevLog(world.getChunkAt(pos), pos, log);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("EnergySides", writeSides(energySides));
        compound.putString("EnergySignal", energyActiveSignal.name());
        compound.putInt("EnergyPerTick", energyPerTick);

        compound.put("LiquidSides", writeSides(liquidSides));
        compound.putString("LiquidSignal", liquidActiveSignal.name());
        compound.putInt("LiquidPerTick", liquidPerTick);
        compound.put("Liquid", liquid.serializeNBT());

        compound.put("GasSides", writeSides(gasSides));
        compound.putString("GasSignal", gasActiveSignal.name());
        compound.putInt("GasPerTick", gasPerTick);
        compound.put("Gas", gas.serializeNBT());

        compound.put("InvSides", writeSides(invSides));
        compound.putString("InvSignal", invActiveSignal.name());
        compound.put("Inv", inv.serializeNBT());

        return super.write(compound);
    }

    private ListNBT writeSides(EnumSet<Side> sides) {
        ListNBT sidesNbt = new ListNBT();
        int i = 0;
        for (Side side : Side.values()) {
            if (sides.contains(side)) {
                sidesNbt.add(i++, StringNBT.valueOf(side.name()));
            }
        }

        return sidesNbt;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        readSides(nbt, "EnergySides", energySides::add);
        energyActiveSignal = ActiveSignal.valueOf(nbt.getString("EnergySignal"));
        energyPerTick = nbt.getInt("EnergyPerTick");

        readSides(nbt, "LiquidSides", liquidSides::add);
        liquidActiveSignal = ActiveSignal.valueOf(nbt.getString("LiquidSignal"));
        liquidPerTick = nbt.getInt("LiquidPerTick");
        liquid.deserializeNBT(nbt.getCompound("Liquid"));

        readSides(nbt, "GasSides", gasSides::add);
        gasActiveSignal = ActiveSignal.valueOf(nbt.getString("GasSignal"));
        gasPerTick = nbt.getInt("GasPerTick");
        gas.deserializeNBT(nbt.getCompound("Gas"));

        readSides(nbt, "InvSides", invSides::add);
        invActiveSignal = ActiveSignal.valueOf(nbt.getString("InvSignal"));
        inv.deserializeNBT(nbt.getCompound("Inv"));
    }

    private void readSides(CompoundNBT nbt, String key, Consumer<Side> consumer) {
        nbt.getList(key, Constants.NBT.TAG_STRING).forEach(tag -> consumer.accept(Side.valueOf(tag.getString())));
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT nbt) {
        write(nbt);
        return super.writeUpdateTag(nbt);
    }

    @Override
    protected void readUpdateTag(CompoundNBT nbt, BlockState state) {
        read(state, nbt);
        super.readUpdateTag(nbt, state);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
        Side side = Side.fromDirection(direction, Direction.NORTH);

        if (cap == CapabilityEnergy.ENERGY && energySides.contains(side)) {
            return energyHolder.cast();
        }

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (liquidSides.contains(side)) {
                return liquidHolder.cast();
            }

            if (gasSides.contains(side)) {
                return gasHolder.cast();
            }
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && invSides.contains(side)) {
            return invHolder.cast();
        }

        return super.getCapability(cap, direction);
    }

    @Override
    protected void serverTick() {
        boolean isBlockPowered = world.isBlockPowered(pos);

        if (energyActiveSignal.isActive(isBlockPowered)) {
            distributeEnergy();
        }

        if (liquidActiveSignal.isActive(isBlockPowered)) {
            distributeLiquid();
        }

        if (gasActiveSignal.isActive(isBlockPowered)) {
            distributeGas();
        }
    }

    // Non caching implementation
    private void distributeEnergy() {
        Direction facing = Direction.NORTH; // call some kinda method

        List<IEnergyStorage> targets = new ArrayList<>(6);
        for (Side side : Side.external()) {
            Direction direction = side.toDirection(facing);
            TileEntity te = world.getTileEntity(pos.offset(direction));

            if (te != null) {
                te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(targets::add);
            }
        }

        int size = targets.size();

        if (size > 0) {
            int totalReceived = 0;
            int amount = energyPerTick / size; // The division is performed in a int context, meaning the results will be rounded down, which in turn means that the final result will be smaller than expected

            for (IEnergyStorage target : targets) {
                size--;
                int received = target.receiveEnergy(amount, false);

                totalReceived += received;
                if (received != amount && size > 0) {
                    amount = (energyPerTick - totalReceived) / size; // same as above
                }
            }

            energy.extractEnergy(totalReceived, false);
        }
    }

    private void distributeLiquid() {
        Direction facing = Direction.NORTH;

        List<IFluidHandler> targets = new ArrayList<>(6);
        for (Side side : Side.external()) {
            Direction direction = side.toDirection(facing);
            TileEntity te = world.getTileEntity(pos.offset(direction));

            if (te != null) {
                te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(targets::add);
            }
        }

        int size = targets.size();

        if (size > 0) {
            int totalReceived = 0;
            int amount = liquidPerTick / size;
            Fluid fluid = liquid.getFluid();

            for (IFluidHandler target : targets) {
                size--;
                int received = target.fill(new FluidStack(fluid, amount), IFluidHandler.FluidAction.EXECUTE);

                totalReceived += received;
                if (received != amount && size > 0) {
                    amount = (liquidPerTick - totalReceived) / size;
                }
            }

            liquid.drain(new FluidStack(fluid, totalReceived), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private void distributeGas() {
        Direction facing = Direction.NORTH;

        List<IFluidHandler> targets = new ArrayList<>(6);
        for (Side side : Side.external()) {
            Direction direction = side.toDirection(facing);
            TileEntity te = world.getTileEntity(pos.offset(direction));

            if (te != null) {
                te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(targets::add);
            }
        }

        int size = targets.size();

        if (size > 0) {
            int totalReceived = 0;
            int amount = gasPerTick / size;
            Fluid fluid = gas.getFluid();

            for (IFluidHandler target : targets) {
                size--;
                int received = target.fill(new FluidStack(fluid, amount), IFluidHandler.FluidAction.EXECUTE);

                totalReceived += received;
                if (received != amount && size > 0) {
                    amount = (gasPerTick - totalReceived) / size;
                }
            }

            gas.drain(new FluidStack(fluid, totalReceived), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    // TODO: 12/20/2020 Implement
    public void distributeItems() {
        Direction facing = Direction.NORTH;

        List<IItemHandler> targets = new ArrayList<>(6);
        for (Side side : Side.external()) {
            Direction direction = side.toDirection(facing);
            TileEntity te = world.getTileEntity(pos.offset(direction));

            if (te != null) {
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()).ifPresent(targets::add);
            }
        }

        int size = targets.size();

        if (size > 0) {
            for (IItemHandler target : targets) {
                for (int i = 0; i < target.getSlots(); i++) {

                }
            }
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("/dev/block");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new DevBlockContainer(id, this);
    }

    public void appendLog(String log) {
        this.logs.add(log);
    }

    public void configureEnergy(int energyPerTick, EnumSet<Side> sides, ActiveSignal signal) {
        this.energyPerTick = energyPerTick;
        energySides = sides;
        energyActiveSignal = signal;
//        Techworks.LOGGER.debug("Energy Sync: EpT = {}, S = {}, AS = {}", energyPerTick, sides, signal);
    }

    public void configureLiquid(int liquidPerTick, Fluid liquid, EnumSet<Side> sides, ActiveSignal signal) {
        this.liquidPerTick = liquidPerTick;
        this.liquid.setFluid(liquid);
        liquidSides = sides;
        liquidActiveSignal = signal;
//        Techworks.LOGGER.debug("Liquid Sync: LpT = {}, L = {}, S = {}, AS = {}", liquidPerTick, liquid, sides, signal);
    }

    public void configureGas(int gasPerTick, Fluid gas, EnumSet<Side> sides, ActiveSignal signal) {
        this.gasPerTick = gasPerTick;
        this.gas.setFluid(gas);
        gasSides = sides;
        gasActiveSignal = signal;
//        Techworks.LOGGER.debug("Gas Sync: GpT = {}, G = {}, S = {}, AS = {}", gasPerTick, gas, sides, signal);
    }

    public void configureItem(List<ItemStack> inv, EnumSet<Side> sides, ActiveSignal signal) {
        this.inv.setStacks(inv);
        invSides = sides;
        invActiveSignal = signal;
//        Techworks.LOGGER.debug("Item Sync: Inv = {}, S = {}, AS = {}", inv, sides, signal);
    }

    public List<String> getLogs() {
        return logs;
    }

    public ActiveSignal getEnergySignal() {
        return energyActiveSignal;
    }

    public EnumSet<Side> getEnergySides() {
        return energySides;
    }

    public int getEnergyPerTick() {
        return energyPerTick;
    }

    public int getLiquidPerTick() {
        return liquidPerTick;
    }

    public Fluid getLiquid() {
        return liquid.getFluid();
    }

    public EnumSet<Side> getLiquidSides() {
        return liquidSides;
    }

    public ActiveSignal getLiquidActiveSignal() {
        return liquidActiveSignal;
    }

    public Fluid getGas() {
        return gas.getFluid();
    }

    public ActiveSignal getGasActiveSignal() {
        return gasActiveSignal;
    }

    public EnumSet<Side> getGasSides() {
        return gasSides;
    }

    public int getGasPerTick() {
        return gasPerTick;
    }

    public List<ItemStack> getInv() {
        return inv.getStacks();
    }

    public ActiveSignal getInvActiveSignal() {
        return invActiveSignal;
    }

    public EnumSet<Side> getInvSides() {
        return invSides;
    }

    public enum ActiveSignal {
        NEVER,
        LOW,
        HIGH,
        ALWAYS;

        public ActiveSignal next() {
            switch (this) {
                case NEVER:
                    return LOW;
                case LOW:
                    return HIGH;
                case HIGH:
                    return ALWAYS;
                default:
                    return NEVER;
            }
        }

        public boolean reactsToSignal() {
            return this == LOW || this == HIGH;
        }

        public boolean isActive(boolean isBlockPowered) {
            return (reactsToSignal() && ((isBlockPowered && this == HIGH) || (!isBlockPowered && this == LOW))) || this == ALWAYS;
        }
    }
}
