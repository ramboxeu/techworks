package io.github.ramboxeu.techworks.common.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.DevUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CapabilitiesCommand {
//    private static final Direction[] DIRS = new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, null };
    private static final Capability<?>[] CAPS = new Capability<?>[] {CapabilityEnergy.ENERGY, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY };

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("capabilities")
                .then(Commands.literal("get")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().asPlayer();
                                    World world = player.getServerWorld();
                                    TileEntity tile = world.getTileEntity(BlockPosArgument.getBlockPos(ctx, "pos"));

                                    if (tile != null) {
                                        Direction facing = player.getHorizontalFacing();

                                        StringBuilder buffer = new StringBuilder();
                                        for (Capability<?> cap : CAPS) {
                                            LazyOptional<?> holder = tile.getCapability(cap, facing);

                                            buffer.append(DevUtils.getCapName(cap)).append(" : ");

                                            if (holder.isPresent()) {
                                                Object value = holder.orElse(null);
                                                buffer.append(value.getClass().getSimpleName()).append("@").append(Integer.toHexString(value.hashCode()));
                                            } else {
                                                buffer.append("EMPTY");
                                            }

                                            buffer.append("\n");
                                        }

                                        Techworks.LOGGER.debug("Caps: \n{}", buffer.toString());
                                        ctx.getSource().sendFeedback(new StringTextComponent(buffer.toString()), false);
                                    }

                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("invalidate")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().asPlayer();
                                    World world = player.getServerWorld();
                                    TileEntity tile = world.getTileEntity(BlockPosArgument.getBlockPos(ctx, "pos"));

                                    if (tile != null) {
                                        try {
                                            // Hacky, hacky, test code of doom
                                            Method method = CapabilityProvider.class.getDeclaredMethod("invalidateCaps");
                                            method.setAccessible(true);
                                            method.invoke(tile);
                                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("validate")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().asPlayer();
                                    World world = player.getServerWorld();
                                    TileEntity tile = world.getTileEntity(BlockPosArgument.getBlockPos(ctx, "pos"));

                                    if (tile != null) {
                                        try {
                                            // Hacky, hacky, test code of doom
                                            Method method = CapabilityProvider.class.getDeclaredMethod("reviveCaps");
                                            method.setAccessible(true);
                                            method.invoke(tile);
                                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    return 1;
                                })
                        )
                )
        );
    }
}
