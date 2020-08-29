package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.wrench.IWrenchable;
import io.github.ramboxeu.techworks.api.wrench.WrenchAction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class WrenchItem extends Item {

    public WrenchItem() {
        super(new Item.Properties().maxDamage(320).group(Techworks.ITEM_GROUP));
    }

    // Called by event for consistency
    public Result onLeftClickBlock(PlayerEntity player, World world, BlockPos pos, Direction face, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(pos);
        boolean flag = tile instanceof IWrenchable;

        if (block instanceof IWrenchable) {
            IWrenchable blockWrenchable = ((IWrenchable) block);
            IWrenchable tileWrenchable = null;

            if (flag) {
                tileWrenchable = (IWrenchable) tile;
            }

            if (!player.isSneaking()) {
                if (blockWrenchable.configure(world, pos, face)){
                    return Result.SUCCESS;
                } else {
                    if (flag && tileWrenchable.configure(world, pos, face)) {
                        return Result.SUCCESS;
                    }
                }
            }
        }

        return Result.BLOCK;
    }

    // Called by event for consistency and flexibility
    public Result onRightClick(PlayerEntity player, World world, BlockPos pos, Direction face, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(pos);
        boolean flag = tile instanceof IWrenchable;

        if (block instanceof IWrenchable) {
            IWrenchable blockWrenchable = (IWrenchable) block;
            IWrenchable tileWrenchable = null;

            if (flag) {
                tileWrenchable = (IWrenchable) tile;
            }

            if (player.isSneaking()) {
                ItemStack blockStack = blockWrenchable.dismantle(state, pos, world);

                if (!blockStack.isEmpty()) {
                    Block.spawnAsEntity(world, pos, blockStack);
                    return Result.SUCCESS;
                } else {
                    if (flag) {
                        ItemStack tileStack = tileWrenchable.dismantle(state, pos, world);

                        if (!tileStack.isEmpty()) {
                            Block.spawnAsEntity(world, pos, tileStack);
                            return Result.SUCCESS;
                        }
                    }
                }
            } else {
                if (blockWrenchable.rotate(state, pos, face, world)) {
                    return Result.SUCCESS;
                } else {
                    if (flag && tileWrenchable.rotate(state, pos, face, world)) {
                        return Result.SUCCESS;
                    }
                }
            }
        }

        return Result.BLOCK;
    }

    public static Result useExternalWrench(WrenchAction action, World world, BlockPos pos, Direction face, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(pos);
        boolean flag = tile instanceof IWrenchable;

        if (block instanceof IWrenchable) {
            IWrenchable blockWrenchable = (IWrenchable) block;
            IWrenchable tileWrenchable = null;

            if (flag) {
                tileWrenchable = (IWrenchable) tile;
            }

            switch (action) {
                case NONE:
                    return Result.BLOCK_ITEM;
                case ROTATE:
                    if (blockWrenchable.rotate(state, pos, face, world)) {
                        return Result.SUCCESS;
                    } else {
                        if (flag && tileWrenchable.rotate(state, pos, face, world)) {
                            return Result.SUCCESS;
                        }
                    }
                case DISMANTLE:
                    ItemStack blockStack = blockWrenchable.dismantle(state, pos, world);

                    if (!blockStack.isEmpty()) {
                        Block.spawnAsEntity(world, pos, blockStack);
                        return Result.SUCCESS;
                    } else {
                        if (flag) {
                            ItemStack tileStack = tileWrenchable.dismantle(state, pos, world);

                            if (!tileStack.isEmpty()) {
                                Block.spawnAsEntity(world, pos, tileStack);
                                return Result.SUCCESS;
                            }
                        }
                    }
                case CONFIGURE:
                    if (blockWrenchable.configure(world, pos, face)){
                        return Result.SUCCESS;
                    } else {
                        if (flag && tileWrenchable.configure(world, pos, face)) {
                            return Result.SUCCESS;
                        }
                    }
            }
        }

        return Result.BLOCK_ITEM;
    }

    public enum Result {
        SUCCESS(true, ActionResultType.SUCCESS, Event.Result.DENY, Event.Result.DENY), // cancels the event
        BLOCK(false, ActionResultType.PASS, Event.Result.ALLOW, Event.Result.DENY), // allows block, denys item
        ITEM(false, ActionResultType.PASS, Event.Result.DENY, Event.Result.ALLOW), // allows item, denys block
        BLOCK_ITEM(false, ActionResultType.PASS, Event.Result.ALLOW, Event.Result.ALLOW); // allows both

        private final boolean cancel;
        private final ActionResultType resultType;
        private final Event.Result blockResult;
        private final Event.Result itemResult;

        Result(boolean cancel, ActionResultType resultType, Event.Result blockResult, Event.Result itemResult) {
            this.cancel = cancel;
            this.resultType = resultType;
            this.blockResult = blockResult;
            this.itemResult = itemResult;
        }

        public boolean cancelsEvent() {
            return cancel;
        }

        public ActionResultType getResultType() {
            return resultType;
        }

        public Event.Result getBlockResult() {
            return blockResult;
        }

        public Event.Result getItemResult() {
            return itemResult;
        }
    }
}
