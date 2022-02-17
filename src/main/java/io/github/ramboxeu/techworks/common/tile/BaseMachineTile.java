package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.capability.HandlerStorage;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.util.ItemUtils;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.RedstoneMode;
import io.github.ramboxeu.techworks.common.util.StandbyMode;
import io.github.ramboxeu.techworks.common.util.machineio.MachinePort;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class BaseMachineTile extends BaseIOTile implements INamedContainerProvider, IWrenchable {
    protected ComponentStorage components;
    protected RedstoneMode redstoneMode;
    protected StandbyMode standbyMode;
    protected HandlerStorage handlers;

    public BaseMachineTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        redstoneMode = RedstoneMode.IGNORE;
        standbyMode = StandbyMode.OFF;
        handlers = new HandlerStorage();
    }

    // PASS continues the execution on the block side
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    public void onLeftClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {}

    public void onNeighborChange(BlockState neighborState, BlockPos neighborPos, Direction side) {
        Techworks.LOGGER.debug("onNeighborChange: neighborState = {}, neighborPos = {}, side = {}", neighborState, neighborPos, side);

        TileEntity tile = world.getTileEntity(neighborPos);
        if (tile != null) {
            if (handlers.isEnabled(HandlerStorage.ITEM))
                handlers.store(side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()));

            if (handlers.isEnabled(HandlerStorage.LIQUID) || handlers.isEnabled(HandlerStorage.GAS))
                handlers.store(side, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()));

            if (handlers.isEnabled(HandlerStorage.ENERGY))
                handlers.store(side, CapabilityEnergy.ENERGY, tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()));
        }
    }

    public Collection<ItemStack> getDrops() {
        return ItemUtils.collectContents(components);
    }

    public ComponentStorage getComponentStorage() {
        return components;
    }

    protected void setWorkingState(boolean isWorking) {
        world.setBlockState(pos, getBlockState().with(TechworksBlockStateProperties.RUNNING, isWorking));
    }

    protected void discoverHandlers() {
        for (Direction side : Direction.values()) {
            TileEntity tile = world.getTileEntity(pos.offset(side));

            if (tile != null) {
                if (handlers.isEnabled(HandlerStorage.ITEM))
                    handlers.store(side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()));

                if (handlers.isEnabled(HandlerStorage.LIQUID) || handlers.isEnabled(HandlerStorage.GAS))
                    handlers.store(side, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()));

                if (handlers.isEnabled(HandlerStorage.ENERGY))
                    handlers.store(side, CapabilityEnergy.ENERGY, tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()));
            }
        }
    }

    @Override
    protected void onFirstTick() {
        super.onFirstTick();
        discoverHandlers();
    }

    @Override
    public void tick() {
        super.tick();
        components.tick();
    }

    @Override
    protected void serverTick() {
        for (Direction side : Direction.values()) {
            MachinePort port = machineIO.getPort(side);

            for (HandlerConfig config : port.getItemConfigs()) {
                if (handlers.getItem(side).isEmpty())
                    break;

                IItemHandler self = (IItemHandler) config.getBaseData().getObject();
                IItemHandler handler = handlers.getItem(side).get(0);

                if (config.getAutoMode().isPull() && config.getMode().canInput() && config.getBaseData().canAutoPull()) {
                    for (int i = 0, size = handler.getSlots(); i < size; i++) {
                        ItemStack stack = handler.extractItem(i, handler.getSlotLimit(i), true);
                        ItemStack remainder = ItemUtils.insertItem(self, stack, false);

                        if (!remainder.isEmpty()) {
                            stack.shrink(remainder.getCount());
                        }

                        handler.extractItem(i, stack.getCount(), false);
                    }
                }

                if (config.getAutoMode().isPush() && config.getMode().canOutput() && config.getBaseData().canAutoPush()) {
                    for (int i = 0, size = self.getSlots(); i < size; i++) {
                        ItemStack stack = self.extractItem(i, handler.getSlotLimit(i), true);
                        ItemStack remainder = ItemUtils.insertItem(handler, stack, false);

                        if (!remainder.isEmpty()) {
                            stack.shrink(remainder.getCount());
                        }

                        self.extractItem(i, stack.getCount(), false);
                    }
                }
            }

            for (HandlerConfig config : port.getEnergyConfigs()) {
                if (handlers.getEnergy(side).isEmpty())
                    break;

                IEnergyStorage storage = handlers.getEnergy(side).get(0);
                IEnergyStorage self = (IEnergyStorage) config.getBaseData().getObject();

                if (config.getAutoMode().isPull() && config.getMode().canInput() && config.getBaseData().canAutoPull()) {
                    int energy = storage.extractEnergy(Integer.MAX_VALUE, true);
                    int consumed = self.receiveEnergy(energy, false);

                    storage.extractEnergy(consumed, false);
                }

                if (config.getAutoMode().isPush() && config.getMode().canOutput() && config.getBaseData().canAutoPush()) {
                    int energy = self.extractEnergy(Integer.MAX_VALUE, true);
                    int consumed = storage.receiveEnergy(energy, false);

                    self.extractEnergy(consumed, false);
                }
            }

            for (HandlerConfig config : port.getGasConfigs()) {
                if (handlers.getGas(side).isEmpty())
                    break;

                IFluidTank self = (IFluidTank) config.getBaseData().getObject();
                IFluidHandler handler = handlers.getGas(side).get(0);

                if (config.getAutoMode().isPull() && config.getMode().canInput() && config.getBaseData().canAutoPull()) {
                    FluidStack fluid = self.getFluid();

                    if (fluid.isEmpty()) {
                        for (int i = 0, size = handler.getTanks(); i < size; i++) {
                            FluidStack stored = handler.getFluidInTank(i);

                            if (!stored.isEmpty()) {
                                fluid = stored.copy();
                                break;
                            }
                        }
                    } else {
                        fluid = fluid.copy();
                    }

                    fluid.setAmount(Integer.MAX_VALUE);

                    FluidStack drained = handler.drain(fluid, IFluidHandler.FluidAction.SIMULATE);

                    if (!drained.isEmpty()) {
                        int filled = self.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                        drained.setAmount(filled);
                        handler.drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    }
                }

                if (config.getAutoMode().isPush() && config.getMode().canOutput() && config.getBaseData().canAutoPush()) {
                    if (!self.getFluid().isEmpty()) {
                        FluidStack fluid = self.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                        int filled = handler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                        self.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }

            for (HandlerConfig config : port.getLiquidConfigs()) {
                if (handlers.getLiquid(side).isEmpty())
                    break;

                IFluidTank self = (IFluidTank) config.getBaseData().getObject();
                IFluidHandler handler = handlers.getLiquid(side).get(0);

                if (config.getAutoMode().isPull() && config.getMode().canInput() && config.getBaseData().canAutoPull()) {
                    FluidStack fluid = self.getFluid();

                    if (fluid.isEmpty()) {
                        for (int i = 0, size = handler.getTanks(); i < size; i++) {
                            FluidStack stored = handler.getFluidInTank(i);

                            if (!stored.isEmpty()) {
                                fluid = stored.copy();
                                break;
                            }
                        }
                    } else {
                        fluid = fluid.copy();
                    }

                    fluid.setAmount(Integer.MAX_VALUE);

                    FluidStack drained = handler.drain(fluid, IFluidHandler.FluidAction.SIMULATE);

                    if (!drained.isEmpty()) {
                        int filled = self.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                        drained.setAmount(filled);
                        handler.drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    }
                }

                if (config.getAutoMode().isPush() && config.getMode().canOutput() && config.getBaseData().canAutoPush()) {
                    if (!self.getFluid().isEmpty()) {
                        FluidStack fluid = self.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                        int filled = handler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                        self.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }

        if (standbyMode.canWork() && redstoneMode.canWork(world.isBlockPowered(pos))) {
            workTick();
        }
    }

    protected void workTick() {}

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("MachineIO", machineIO.serializeNBT());
        tag.put("Components", components.serializeNBT());
        NBTUtils.serializeEnum(tag, "StandbyMode", standbyMode);
        NBTUtils.serializeEnum(tag, "RedstoneMode", redstoneMode);

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        machineIO.deserializeNBT(tag.getCompound("MachineIO"));
        components.deserializeNBT(tag.getCompound("Components"));
        standbyMode = NBTUtils.deserializeEnum(tag, "StandbyMode", StandbyMode.class);
        redstoneMode = NBTUtils.deserializeEnum(tag, "RedstoneMode", RedstoneMode.class);

        if (redstoneMode == null) redstoneMode = RedstoneMode.IGNORE;
        if (standbyMode == null) standbyMode = StandbyMode.OFF;

        super.read(state, tag);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("MachineIO", machineIO.serializeNBT());
        tag.put("Components", components.serializeNBT());
        NBTUtils.serializeEnum(tag, "StandbyMode", standbyMode);
        NBTUtils.serializeEnum(tag, "RedstoneMode", redstoneMode);

        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT nbt, BlockState state) {
        machineIO.deserializeNBT(nbt.getCompound("MachineIO"));
        components.deserializeNBT(nbt.getCompound("Components"));
        standbyMode = NBTUtils.deserializeEnum(nbt, "StandbyMode", StandbyMode.class);
        redstoneMode = NBTUtils.deserializeEnum(nbt, "RedstoneMode", RedstoneMode.class);

        super.readUpdateTag(nbt, state);
    }

    @Override
    public boolean rotate(BlockState state, BlockPos pos, @Nullable Direction face, World world) {
        if (face != null && face.getAxis() != Direction.Axis.Y) {
            Direction facing = state.get(HORIZONTAL_FACING);
            Direction rotated = facing.rotateY();
            world.setBlockState(pos, state.with(HORIZONTAL_FACING, rotated));

//            if (!world.isRemote) {
//                TileEntity te = world.getTileEntity(pos);
//
//                if (te instanceof BaseMachineTile) {
//                    MachineIO machineIO = ((BaseMachineTile) te).getMachineIO();
////                    machineIO.setSideStatus(facing, false);
////                    machineIO.setSideStatus(rotated, true);
//                    TechworksPacketHandler.sendMachinePortUpdatePacket(pos, rotated.getIndex(), machineIO.getPort(rotated), world.getChunkAt(pos));
//                }
//            }

            return true;
        }

        return false;
    }

    @Nonnull
    public Direction getFacing() {
        return world != null ? getBlockState().get(HORIZONTAL_FACING) : Direction.NORTH;
    }

    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public StandbyMode getWorkMode() {
        return standbyMode;
    }

    public void syncRedstoneMode(RedstoneMode mode) {
        setRedstoneMode(mode);
        TechworksPacketHandler.syncMachineWorkState(pos, mode, null);
    }

    public void syncStandbyMode(StandbyMode mode) {
        setStandbyMode(mode);
        TechworksPacketHandler.syncMachineWorkState(pos, null, mode);
    }

    public void setRedstoneMode(RedstoneMode mode) {
        if (mode != null)
            redstoneMode = mode;
    }

    public void setStandbyMode(StandbyMode mode) {
        if (mode != null)
            standbyMode = mode;
    }
}
