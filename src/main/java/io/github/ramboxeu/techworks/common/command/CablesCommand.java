package io.github.ramboxeu.techworks.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.ramboxeu.techworks.common.tile.BaseCableTile;
import io.github.ramboxeu.techworks.common.util.cable.network.*;
import io.github.ramboxeu.techworks.common.util.cable.pathfinding.Pathfinder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.server.command.EnumArgument;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CablesCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("cables")
                .then(registerInfo())
                .then(registerNetwork())
                .then(registerTarget())
                .then(registerWrapper())
                .then(registerSimulate())
        );
    }

    private static CommandNode<CommandSource> registerSimulate() {
        return Commands.literal("simulate")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("side", EnumArgument.enumArgument(Direction.class))
                            .then(Commands.argument("type", EnumArgument.enumArgument(TransferType.class))
                                    .executes(ctx -> {
                                        PlayerEntity player = ctx.getSource().asPlayer();
                                        World world = player.getEntityWorld();
                                        BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                                        TileEntity te = world.getTileEntity(pos);
                                        Direction dir = ctx.getArgument("side", Direction.class);
                                        TransferType type = ctx.getArgument("type", TransferType.class);

                                        if (te instanceof BaseCableTile) {
                                            BaseCableTile cable = (BaseCableTile) te;
                                            IEndpointNode endpoint = cable.getNetwork().getTransferTarget(type, (IEndpointNode) cable.getNeighboursMap().get(dir));

                                            if (endpoint != null) {
                                                sendDebugMessage(player, "Finding path: %s -> %s", cable.getPos(), endpoint.getPosition());

                                                List<BlockPos> path = Pathfinder.findPath(cable, endpoint);

                                                for (int i = 0; i < path.size(); i++) {
                                                    sendDebugMessage(player, "[%s] = %s", i, path.get(i));
                                                }
                                            } else {
                                                sendDebugMessage(player, "Target not found");
                                            }
                                        }

                                        return 0;
                                    })
                            )
                        )
                )
                .build();
    }

    private static CommandNode<CommandSource> registerWrapper() {
        return Commands.literal("wrapper")
                .then(Commands.argument("wrapper", BlockPosArgument.blockPos())
                        .then(Commands.argument("cable", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    PlayerEntity player = ctx.getSource().asPlayer();
                                    World world = player.getEntityWorld();
                                    BlockPos wrapperPos = BlockPosArgument.getBlockPos(ctx, "wrapper");
                                    BlockPos cablePos = BlockPosArgument.getBlockPos(ctx, "cable");
                                    TileEntity wrapperTile = world.getTileEntity(wrapperPos);
                                    TileEntity cableTile = world.getTileEntity(cablePos);

                                    if (wrapperTile != null && cableTile instanceof BaseCableTile) {

                                    }

                                    return 1;
                                })
                        )
                )
                .build();
    }

    private static CommandNode<CommandSource> registerTarget() {
        return Commands.literal("target")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("side", EnumArgument.enumArgument(Direction.class))
                                .then(Commands.argument("type", EnumArgument.enumArgument(TransferType.class))
                                        .executes(ctx -> {
                                            PlayerEntity player = ctx.getSource().asPlayer();
                                            World world = player.getEntityWorld();
                                            BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                                            TileEntity te = world.getTileEntity(pos);
                                            Direction dir = ctx.getArgument("side", Direction.class);
                                            TransferType type = ctx.getArgument("type", TransferType.class);

                                            if (te instanceof BaseCableTile) {
                                                BaseCableTile cable = (BaseCableTile) te;
                                                IEndpointNode endpoint = cable.getNetwork().getTransferTarget(type, (IEndpointNode) cable.getNeighboursMap().get(dir));

                                                if (endpoint != null) {
                                                    sendDebugMessage(player, "Transfer (NORMAL)");
                                                    sendDebugMessage(player, "|- From: %s (%s) on %s", cable.getClass().getSimpleName(), Integer.toHexString(cable.hashCode()), cable.getPosition());
                                                    sendDebugMessage(player, "|- To: %s (%s) on %s", endpoint.getClass().getSimpleName(), Integer.toHexString(endpoint.hashCode()), endpoint.getPosition());
                                                } else {
                                                    sendDebugMessage(player, "Transfer (NORMAL)");
                                                    sendDebugMessage(player, "|- From: %s (%s) on %s", cable.getClass().getSimpleName(), Integer.toHexString(cable.hashCode()), cable.getPosition());
                                                    sendDebugMessage(player, "|- To: no target");
                                                }
                                            }

                                            return 0;
                                        })
                                )
                        )
                )
                .build();
    }

    private static CommandNode<CommandSource> registerNetwork() {
        return Commands.literal("network")
                .then(Commands.argument("id", UUIDArgument.func_239194_a_())
                        .executes(ctx -> {
                            PlayerEntity player = ctx.getSource().asPlayer();
                            World world = player.getEntityWorld();
                            UUID networkId = UUIDArgument.func_239195_a_(ctx, "id");

                            runNetwork(player, world, networkId);
                            return 0;
                        })
                )
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(ctx -> {
                            PlayerEntity player = ctx.getSource().asPlayer();
                            World world = player.getEntityWorld();
                            BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                            TileEntity te = world.getTileEntity(pos);

                            if (te instanceof BaseCableTile) {
                                BaseCableTile cable = (BaseCableTile) te;
                                BaseCableNetwork network = cable.getNetwork();
                                runNetwork(player, world, network.getId());
                            }

                            return 0;
                        })
                )
                .build();
    }

    private static void runNetwork(PlayerEntity player, World world, UUID networkId) {
        ICableNetwork network = CableNetworkManager.get(world).getNetwork(networkId);

        if (network != null) {
            sendDebugMessage(player, "Network %s", networkId);
            sendDebugMessage(player, "|- Endpoints:");

            int i = 0;
            for (IEndpointNode endpoint : network.getAllEndpoints()) {
                sendDebugMessage(player, "|-- [%d] = %s (%s)", i++, endpoint.getClass().getSimpleName(), Integer.toHexString(endpoint.hashCode()));
            }
        }
    }

    private static CommandNode<CommandSource> registerInfo() {
        return Commands.literal("info")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(ctx -> {
                            PlayerEntity player = ctx.getSource().asPlayer();
                            World world = player.getEntityWorld();
                            BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                            TileEntity te = world.getTileEntity(pos);

                            if (te instanceof BaseCableTile) {
                                BaseCableTile cable = (BaseCableTile) te;
                                BaseCableNetwork network = cable.getNetwork();

                                sendDebugMessage(player, "BaseCableTile on %s", pos);
                                sendDebugMessage(player, "|- Network:");
                                sendDebugMessage(player, "|-- Id: %s", network.getId());
                                sendDebugMessage(player, "|- Neighbours:");

                                for (Map.Entry<Direction, INode> entry : cable.getNeighboursMap().entrySet()) {
                                    INode node = entry.getValue();
                                    String type = node instanceof IEndpointNode ? "Endpoint" : "Node";

                                    sendDebugMessage(player, "|-- [%s] = %s (%s)", entry.getKey(), type, Integer.toHexString(node.hashCode()));
                                }
                            }

                            return 0;
                        })
                )
                .build();
    }

    private static void sendDebugMessage(PlayerEntity player, String message, Object... args) {
        player.sendStatusMessage(new StringTextComponent(String.format(message, args)), false);
    }
}
