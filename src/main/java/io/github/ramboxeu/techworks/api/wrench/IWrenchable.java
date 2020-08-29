package io.github.ramboxeu.techworks.api.wrench;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Interface for blocks (and tile entities) that be interacted with wrench. In general what happens when is derived from
 * Crescent Hammer (from King Lemming's Thermal Foundation mod). When using this with tile entities please note that:
 * block has to also implement this interface and block implementation will be run first
 * <br/>
 *
 * See {@link io.github.ramboxeu.techworks.api.wrench.IWrench} if you want your wrench to have effect on
 * implementations of this
 */
public interface IWrenchable {

    /**
     * Rotates block 90deg clockwise
     * <br/>Called when player right clicks on a block
     *
     * @param state the state of block that was clicked
     * @param pos the position of block that was clicked
     * @param face the face of block that was clicked
     * @param world the world of block that was clicked
     * @return {@code true} if the block was rotated; {@code false} will continue execution
     */
    default boolean rotate(BlockState state, BlockPos pos, @Nullable Direction face, World world) {
        return false;
    }

    /**
     * Breaks machine with contents saved
     * <br/> Called when player right clicks on a block and is sneaking
     *
     * @param state the state of block that was clicked
     * @param pos the position of block that was clicked
     * @param world the world of block that was clicked
     * @return {@code ItemStack} to be placed into the world; @code ItemStack.EMPTY} will continue execution
     */
    default ItemStack dismantle(BlockState state, BlockPos pos, World world) {
        return ItemStack.EMPTY;
    }

    /**
     * Does all sorts of configurations, eg. configure machine ports
     * <br/> Called when player left clicks on block
     *
     * @param world the world of block that was clicked
     * @param pos the position of block that was clicked
     * @param face the face that was clicked
     * @return {@code true} if the block was configured; {@code false} will continue execution
     */
    // TODO: Hit vector
    default boolean configure(World world, BlockPos pos, Direction face) {
        return false;
    }

//    /**
//     * Called when player right clicks on a block and is sneaking
//     * <br/>Techworks use: open ComponentsScreen
//     */
//    void openComponents(BlockPos pos, PlayerEntity player, World world);
}
