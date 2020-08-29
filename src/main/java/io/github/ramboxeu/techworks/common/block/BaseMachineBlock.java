package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.network.TechworksPacketHandler;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.tile.machine.MachineIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static io.github.ramboxeu.techworks.common.property.TechworksBlockStateProperties.RUNNING;
import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;

public abstract class BaseMachineBlock extends Block implements IWrenchable {
    public BaseMachineBlock() {
        super(Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(5, 6).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, RUNNING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(RUNNING, false);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof BaseMachineTile) {
            ActionResultType result = ((BaseMachineTile) tileEntity).onRightClick(state, worldIn, pos, player, handIn, rayTraceResult);

            if (result != ActionResultType.PASS) {
                return result;
            }
        }

        if (!worldIn.isRemote) {

            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Container provider is missing!");
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof BaseMachineTile) {
                BaseMachineTile machineTile = (BaseMachineTile) te;
                for (ItemStack stack : machineTile.getDrops()) {
                    spawnAsEntity(world, pos, stack);
                }
            }

            world.removeTileEntity(pos);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        CompoundNBT stackTag = stack.getOrCreateTag();

        if (te instanceof BaseMachineTile && stackTag.contains("TileEntity", TAG_COMPOUND)) {
            CompoundNBT teNbt = stackTag.getCompound("TileEntity");
            teNbt.putInt("x", pos.getX());
            teNbt.putInt("y", pos.getY());
            teNbt.putInt("z", pos.getZ());
            te.deserializeNBT(teNbt);
        }
    }

    @Override
    public ItemStack dismantle(BlockState state, BlockPos pos, World world) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            ItemStack blockStack = new ItemStack(getSelf().asItem());

            if (te instanceof BaseMachineTile) {
                blockStack.getOrCreateTag().put("TileEntity", te.serializeNBT());
                world.removeTileEntity(pos);
            }

            world.removeBlock(pos, false);
            return blockStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean configure(World world, BlockPos pos, Direction face) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof BaseMachineTile) {
                MachineIO machineIO = ((BaseMachineTile) te).getMachineIO();
                if (!machineIO.isFaceDisabled(face)) {
                    machineIO.cyclePort(face);
                    machineIO.getPort(face);
                    TechworksPacketHandler.sendMachinePortUpdatePacket(pos, face.getIndex(), machineIO.getPort(face), world.getChunkAt(pos));
                }
            }
        }

        return true;
    }
}
