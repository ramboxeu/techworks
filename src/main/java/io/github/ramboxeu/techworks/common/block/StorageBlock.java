package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.common.tile.StorageTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class StorageBlock extends Block implements IWrenchable {

    public StorageBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        CompoundNBT stackTag = stack.getTag();

        if (tile != null && stackTag != null && stackTag.contains("Tile", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tag = stackTag.getCompound("Tile");
            BlockPos tilePos = tile.getPos();
            tag.putInt("x", tilePos.getX());
            tag.putInt("y", tilePos.getY());
            tag.putInt("z", tilePos.getZ());
            tile.deserializeNBT(state, tag);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof StorageTile) {
            return ((StorageTile<?>) tile).onRightClick(player, hand) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && (state.getBlock() != newState.getBlock() || !newState.hasTileEntity())) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof StorageTile) {
                for (ItemStack stack : ((StorageTile<?>) tile).getDrops()) {
                    spawnAsEntity(world, pos, stack);
                }
            }

            world.removeTileEntity(pos);
        }
    }

    @Override
    public ItemStack dismantle(BlockState state, BlockPos pos, World world) {
        TileEntity tile = world.getTileEntity(pos);
        ItemStack stack = new ItemStack(this);

        if (tile instanceof StorageTile) {
            stack.getOrCreateTag().put("Tile", tile.serializeNBT());
            world.removeTileEntity(pos);
        }

        world.removeBlock(pos, false);

        return stack;
    }
}
