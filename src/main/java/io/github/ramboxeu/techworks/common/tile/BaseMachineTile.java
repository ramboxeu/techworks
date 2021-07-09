package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.component.ComponentStorage;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class BaseMachineTile extends BaseIOTile implements INamedContainerProvider, IWrenchable {
    protected ComponentStorage components;

    public BaseMachineTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    // PASS continues the execution on the block side
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    public void onLeftClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {}

    public List<ItemStack> getDrops() {
        return Collections.emptyList();
    }

    @Deprecated
    protected abstract void buildComponentStorage(ComponentStorage.Builder builder);

    @Override
    public void tick() {
        super.tick();
        components.tick();
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("MachineIO", machineIO.serializeNBT());
        tag.put("Components", components.serializeNBT());

        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        machineIO.deserializeNBT(tag.getCompound("MachineIO"));
        components.deserializeNBT(tag.getCompound("Components"));

        super.read(state, tag);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT tag) {
        tag.put("MachineIO", machineIO.serializeNBT());
        tag.put("Components", components.serializeNBT());

        return super.writeUpdateTag(tag);
    }

    @Override
    protected void readUpdateTag(CompoundNBT nbt, BlockState state) {
        machineIO.deserializeNBT(nbt.getCompound("MachineIO"));
        components.deserializeNBT(nbt.getCompound("Components"));

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

    @NotNull
    public Direction getFacing() {
        return world != null ? getBlockState().get(HORIZONTAL_FACING) : Direction.NORTH;
    }
}
