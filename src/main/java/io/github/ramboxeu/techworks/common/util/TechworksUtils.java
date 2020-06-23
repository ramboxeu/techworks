package io.github.ramboxeu.techworks.common.util;

import com.google.common.collect.ImmutableSet;
import io.github.ramboxeu.techworks.common.blockentity.machine.AbstractMachineBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

// Shared utils for everything
public class TechworksUtils {
    public static final Set<Direction> HORIZONTAL_DIRECTIONS = ImmutableSet.of(Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST);

    @SuppressWarnings("unchecked")
    public static <T extends AbstractMachineBlockEntity> Optional<AbstractMachineBlockEntity> getMachineBlockEntity(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity != null) {
            if (blockEntity instanceof AbstractMachineBlockEntity) {
                return Optional.of((AbstractMachineBlockEntity) blockEntity);
            }
        }

        return Optional.empty();
    }
}
