package io.github.ramboxeu.techworks.api.wrench;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IWrenchable {

    /**
     * Called when player left clicks on a block
     * <br/>Techworks use: rotate blocks 90deg clockwise
     *
     * @param state the state of block that was clicked
     * @param pos the position of block that was clicked
     * @param face the face of block that was clicked
     * @param world the world of block that was clicked
     */
    void rotate(BlockState state, BlockPos pos, @Nullable Direction face, World world);
    
    /**
     * Called when player left clicks on a block and is sneaking
     * <br/>Techworks use: break machines with contents saved
     */
    void dismantle(BlockState state, BlockPos pos, World world);

    /**
     * Called when player right clicks on block
     * <br/>Techworks use: do all sorts of configurations, eg. toggle pipe connections
     */
    void configure(World world, BlockPos pos, Direction face);

    /**
     * Called when player right clicks on a block and is sneaking
     * <br/>Techworks use: open ComponentsScreen
     */
    void openComponents(BlockPos pos, PlayerEntity player, World world);
}
