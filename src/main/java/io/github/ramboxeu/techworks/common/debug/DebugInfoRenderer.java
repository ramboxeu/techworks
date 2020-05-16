package io.github.ramboxeu.techworks.common.debug;

import io.github.ramboxeu.techworks.common.registration.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public class DebugInfoRenderer {
    public static void render(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && Minecraft.getInstance().currentScreen == null) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            ClientWorld world = Minecraft.getInstance().world;
            FontRenderer font = Minecraft.getInstance().fontRenderer;

            if (player.getHeldItemMainhand().getItem().equals(Registration.DEBUGGER_ITEM.get())) {
                BlockRayTraceResult result = (BlockRayTraceResult) player.pick(20.0D, 0.0F, true);
                if (result.getType() == RayTraceResult.Type.BLOCK) {
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
                    font.drawString("Debugging " + builder.getTitle() + ":", x, y, color);
                    for (DebugInfoBuilder.Section section : builder.getSections()) {
                        font.drawString("- " + section.getName(), x + 5, y + i, color);
                        for (String line : section.getLines()) {
                            i += 9;
                            font.drawString(line, x + 10, y + i, color);
                        }
                        i += 9;
                    }
                }
            }
        }
    }
}
