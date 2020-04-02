package io.github.ramboxeu.techworks.common.util.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {

    public static void drawFluid(int tankX, int tankY, FluidStack fluid, int tankWidth, int tankHeight, int capacity) {
        if (fluid == null || fluid.isEmpty())
            return;

        GL11.glPushMatrix();
        ResourceLocation texture = fluid.getFluid().getAttributes().getStillTexture();
        Color color = new Color(fluid.getFluid().getAttributes().getColor());

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlasTexture(new ResourceLocation("textures/atlas/blocks.png")).getSprite(texture);

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        float v1 = minU + (maxU - minU) * sprite.getWidth() / 16F;
        float v = minV + (maxV - minV) * sprite.getHeight() / 16F;

        int fullHeights = tankHeight / sprite.getHeight();
        int leftHeight = sprite.getHeight() - (tankHeight * fullHeights);
        int fullWidths = tankWidth / sprite.getWidth();
        int leftWidth = sprite.getWidth() - (tankWidth * fullWidths);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);

        for (int i = 0; i < fullHeights; i++) {
            for (int j = 0; j < fullWidths; j++) {
                int x = tankX + tankWidth - (j * sprite.getWidth()) - sprite.getWidth();
                int y = tankY + tankHeight - (i * sprite.getHeight()) - sprite.getHeight();

                buffer.pos(x, y + sprite.getHeight(), 5).color(red, green, blue, alpha).tex(minU, maxV).endVertex();
                buffer.pos(x + sprite.getWidth(), y + sprite.getHeight(), 5).color(red, green, blue, alpha).tex(maxU, maxV).endVertex();
                buffer.pos(x + sprite.getWidth(), y , 5).color(red, green, blue, alpha).tex(maxU, minV).endVertex();
                buffer.pos(x, y, 5).color(red, green, blue, alpha).tex(minU, minV).endVertex();
            }
        }

        Tessellator.getInstance().draw();
        GL11.glPopMatrix();
    }

    private static void drawColoredSprite(TextureAtlasSprite sprite, double x, double y, Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        buffer.pos(x, y + sprite.getHeight(), 5).color(red, green, blue, alpha).tex(minU, maxV).endVertex();
        buffer.pos(x + sprite.getWidth(), y + sprite.getHeight(), 5).color(red, green, blue, alpha).tex(maxU, maxV).endVertex();
        buffer.pos(x + sprite.getWidth(), y , 5).color(red, green, blue, alpha).tex(maxU, minV).endVertex();
        buffer.pos(x, y, 5).color(red, green, blue, alpha).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }
}
