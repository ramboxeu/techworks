package io.github.ramboxeu.techworks.common.tile;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class BaseMachineTile extends BaseIOTile implements INamedContainerProvider, IWrenchable {
//    protected final ComponentStackHandler components;

    public BaseMachineTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);

//        this.components = ComponentStackHandler.withBuilder(builder.onChanged(this::refreshComponents));
    }

    @Override
    protected void onFirstTick() {
//        if (!world.isRemote && machineIO != null) {
////            machineIO.setSideStatus(Side.FRONT, true);
//        }
    }

    // PASS continues the execution on the block side
    public ActionResultType onRightClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }

    public void onLeftClick(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {}

    @Deprecated
    protected void refreshComponents(ItemStack stack, boolean input) {}

    public List<ItemStack> getDrops() {
        return Collections.emptyList();
    }

    protected abstract ITextComponent getComponentsGuiName();

    @Override
    public CompoundNBT write(CompoundNBT compound) {
//        compound.put("ComponentInv", components.serializeNBT());
        compound.put("MachineIO", machineIO.serializeNBT());

        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
//        components.deserializeNBT(compound.getCompound("ComponentInv"));
        machineIO.deserializeNBT(compound.getCompound("MachineIO"));

        super.read(state, compound);
    }

    @Override
    protected CompoundNBT writeUpdateTag(CompoundNBT nbt) {
        nbt.put("MachineIO", machineIO.serializeNBT());
        return super.writeUpdateTag(nbt);
    }

    @Override
    protected void readUpdateTag(CompoundNBT nbt, BlockState state) {
        super.readUpdateTag(nbt, state);
        machineIO.deserializeNBT(nbt.getCompound("MachineIO"));
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
        return world != null ? world.getBlockState(pos).get(HORIZONTAL_FACING) : Direction.NORTH;
    }
}
