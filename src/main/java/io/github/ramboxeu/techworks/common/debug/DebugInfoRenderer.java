package io.github.ramboxeu.techworks.common.debug;

import io.github.ramboxeu.techworks.common.network.DebugRequestPacket;
import io.github.ramboxeu.techworks.common.network.TechworkPacketHandler;
import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;
import java.util.List;

public class DebugInfoRenderer {
    private static BlockPos cachedPos = BlockPos.ZERO;
    private static List<DebugInfoBuilder.Section> serverSideSections;

    public static void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && Minecraft.getInstance().currentScreen == null) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            ClientWorld world = Minecraft.getInstance().world;
            FontRenderer font = Minecraft.getInstance().fontRenderer;

            if (player.getHeldItemMainhand().getItem().equals(Registration.DEBUGGER_ITEM.get())) {
                BlockRayTraceResult result = (BlockRayTraceResult) player.pick(20.0D, 0.0F, true);
                if (result.getType() == RayTraceResult.Type.BLOCK) {
                    if (cachedPos.compareTo(result.getPos()) != 0) {
                        cachedPos = result.getPos();
//                        TechworkPacketHandler.sentDebugRequestPacket(new DebugRequestPacket(cachedPos, world.dimension.getType()));
                    }

                    BlockState state = world.getBlockState(result.getPos());
                    TileEntity te = world.getTileEntity(result.getPos());
                    DebugInfoBuilder builder = state.hasTileEntity() ? new DebugInfoBuilder(state.getBlock().getClass(), te.getClass()) : new DebugInfoBuilder(state.getBlock().getClass());

                    if (state.getBlock() instanceof IDebuggable) {
                        ((IDebuggable) state.getBlock()).addDebugInfo(builder);
                    }

                    if (state.hasTileEntity() && te instanceof IDebuggable) {
                        ((IDebuggable) world.getTileEntity(result.getPos())).addDebugInfo(builder);
                    }

                    int x = 0;
                    int y = 10;
                    int i = 9;
                    int color = new Color(0, 0, 0, 255).getRGB();
//                    font.drawString("Debugging " + builder.getTitle() + ":", x, y, color);

                    if (serverSideSections != null) {
                        builder.getSections().addAll(serverSideSections);
                    }

                    for (DebugInfoBuilder.Section section : builder.getSections()) {
//                        font.drawString("- " + section.getName(), x + 5, y + i, color);
                        for (String line : section.getLines()) {
                            i += 9;
//                            font.drawString(line, x + 10, y + i, color);
                        }
                        i += 9;
                    }
                }
            }
        }
    }

    public static void setServerSideSections(List<DebugInfoBuilder.Section> sections) {
        serverSideSections = sections;
    }
}
