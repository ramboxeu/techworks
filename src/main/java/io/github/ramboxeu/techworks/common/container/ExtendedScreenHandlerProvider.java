package io.github.ramboxeu.techworks.common.container;

import com.sun.istack.internal.Nullable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ExtendedScreenHandlerProvider {
    @Nullable
    ExtendedScreenHandlerFactory createMenu(BlockState state, World world, BlockPos pos);
}
