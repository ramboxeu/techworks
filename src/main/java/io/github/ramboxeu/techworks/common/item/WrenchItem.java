package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.client.container.machine.ComponentsContainer;
import io.github.ramboxeu.techworks.common.component.IComponentsContainerProvider;
import jdk.nashorn.internal.ir.BlockStatement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HopperBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class WrenchItem extends Item {

    public WrenchItem() {
        super(new Item.Properties().maxStackSize(1).group(Techworks.ITEM_GROUP));
    }

    // Called by event for consistency, return true to cancel
    public boolean onLeftClickBlock(PlayerEntity player, World world, BlockPos pos, Direction face, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (player.isSneaking()) {
            if (block instanceof IWrenchable) {
                ((IWrenchable) block).dismantle(state, pos, world);
            }
        } else {
            if (block instanceof IWrenchable) {
                ((IWrenchable) block).rotate(state, pos, face, world);
            } else {
                BlockState rotatedState;

                if (block.isIn(Tags.Blocks.CHESTS)) {
                    rotatedState = block.rotate(state, world, pos, Rotation.CLOCKWISE_90);
                } else if (block == Blocks.HOPPER) {
                    int dirIndex = state.get(HopperBlock.FACING).getIndex() + 1;
                    rotatedState = state.with(HopperBlock.FACING, Direction.byIndex(dirIndex > 5 ? 0 : (dirIndex == 1 ? 2 : dirIndex)));
                } else {
                    return false;
                }

                world.setBlockState(pos, rotatedState);
            }
        }

        return true;
    }

    // Called by event for consistency and flexibility, return false to pass allow Block#onBlockActivated to run
    public boolean onRightClick(PlayerEntity player, World world, BlockPos pos, Direction face, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof IWrenchable) {
            IWrenchable wrenchable = (IWrenchable) block;

            if (player.isSneaking()) {
                wrenchable.openComponents(pos, player, world);
            } else {
                // There will a special case for pipes (and possibly other things) that require hit vector to
                // determine which portion (rather than face) user clicked
                wrenchable.configure(face);
            }
        }

        return true;
    }
}
