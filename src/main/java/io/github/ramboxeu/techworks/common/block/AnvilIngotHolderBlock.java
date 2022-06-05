package io.github.ramboxeu.techworks.common.block;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.item.handler.SingleStackStorage;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.registration.TechworksRecipes;
import io.github.ramboxeu.techworks.common.tile.AnvilIngotHolderTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class AnvilIngotHolderBlock extends Block {

    public AnvilIngotHolderBlock() {
        super(Properties.create(Material.AIR).doesNotBlockMovement());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AnvilIngotHolderTile();
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        return state.isValidPosition(world, currentPos) ? state : Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        return isOnAnvil(world, pos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AnvilIngotHolderTile)
            return VoxelShapes.create(.25, 0, .25, .75, ((AnvilIngotHolderTile) tile).getStack().getCount() / 32.0, .75);

        return VoxelShapes.create(.25, 0, .25, .75, .0625, .75);
    }

    @Override
    public void spawnAdditionalDrops(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        Techworks.LOGGER.debug("Spawn drops!");
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof AnvilIngotHolderTile) {
            ((AnvilIngotHolderTile) tile).dropStack();
        }
    }

    @Override
    public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.matchesBlock(newState.getBlock())) {
            TileEntity tile = world.getTileEntity(pos);
            spawnDrops(oldState, world, pos, tile);
        }

        super.onReplaced(oldState, world, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof AnvilIngotHolderTile) {
            if (((AnvilIngotHolderTile) tile).addItems(player))
                return ActionResultType.SUCCESS;
        }

        return take(world, pos) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    protected boolean isAir(BlockState state) {
        return false;
    }

    @Override
    public boolean isAir(BlockState state, IBlockReader world, BlockPos pos) {
        return isAir(state);
    }

    private boolean isOnAnvil(IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.down()).isIn(BlockTags.ANVIL);
    }

    public static boolean place(World world, BlockPos pos, ItemStack stack) {
        BlockState oldState = world.getBlockState(pos);
        if (world.setBlockState(pos, TechworksBlocks.ANVIL_INGOT_HOLDER.get().getDefaultState())) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof AnvilIngotHolderTile) {
                ((AnvilIngotHolderTile) tile).setStack(stack);
                return true;
            } else {
                world.setBlockState(pos, oldState);
            }
        }

        return false;
    }

    public static boolean take(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == TechworksBlocks.ANVIL_INGOT_HOLDER.get()) {
            return world.removeBlock(pos, false);
        }

        return false;
    }

    public static boolean onRightClick(World world, BlockPos pos, PlayerEntity player) {
        if (world.getBlockState(pos).isIn(BlockTags.ANVIL)) {
            ItemStack handStack = player.getHeldItemMainhand();
            ItemStack offhandStack = player.getHeldItemOffhand();
            int count = player.isSneaking() ? 4 : 2;

            if (hasRecipe(world, handStack, count)) {
                if (!player.abilities.isCreativeMode) {
                    return handStack.getCount() >= count && place(world, pos.up(), handStack.split(count));
                } else {
                    return place(world, pos.up(), new ItemStack(handStack.getItem(), count));
                }
            }

            if (hasRecipe(world, offhandStack, count)) {
                if (!player.abilities.isCreativeMode) {
                    return offhandStack.getCount() >= count && place(world, pos.up(), offhandStack.split(count));
                } else {
                    return place(world, pos.up(), new ItemStack(offhandStack.getItem(), count));
                }
            }

            if (handStack.isEmpty()) {
                return take(world, pos);
            }
        }

        return false;
    }

    private static boolean hasRecipe(World world, ItemStack stack, int count) {
        ItemStack example = stack.copy();
        example.setCount(count);
        return world.getRecipeManager().getRecipe(TechworksRecipes.HAMMERING.get(), new RecipeWrapper(new SingleStackStorage(example)), world).isPresent();
    }
}
