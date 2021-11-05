package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.RedstoneMode;
import io.github.ramboxeu.techworks.common.util.StandbyMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class BaseMachineTile extends BaseIOTile implements INamedContainerProvider, IWrenchable {
    protected ComponentStorage components;
    protected RedstoneMode redstoneMode;
    protected StandbyMode standbyMode;

    public BaseMachineTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        redstoneMode = RedstoneMode.IGNORE;
        standbyMode = StandbyMode.OFF;
    }

    // PASS continues the execution on the block side
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    public void onLeftClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {}

    public List<ItemStack> getDrops() {
        return Collections.emptyList();
    }

    public ComponentStorage getComponentStorage() {
        return components;
    }

    protected void setWorkingState(boolean isWorking) {
        world.setBlockState(pos, getBlockState().with(TechworksBlockStateProperties.RUNNING, isWorking));
    }

    @Override
    public void tick() {
        super.tick();
        components.tick();
    }

    @Override
    protected void serverTick() {
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
