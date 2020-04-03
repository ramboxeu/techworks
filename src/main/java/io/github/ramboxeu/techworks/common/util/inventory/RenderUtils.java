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

    public static void drawFluidInTank(int tankX, int tankY, FluidStack fluid, int tankWidth, int tankHeight, int capacity) {
        if (fluid == null || fluid.isEmpty())
            return;

        GL11.glPushMatrix();
        ResourceLocation texture = fluid.getFluid().getAttributes().getStillTexture();
        Color color = new Color(fluid.getFluid().getAttributes().getColor());

        int height = (int) (((float) fluid.getAmount() / capacity) * tankHeight);
        int y = (tankY + tankHeight) - height;

        drawColoredSpriteTiled(findAndBindBlockTexture(texture), tankX, y, color, tankWidth, height);
        GL11.glPopMatrix();
    }

    private static TextureAtlasSprite findAndBindBlockTexture(ResourceLocation location) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
        return Minecraft.getInstance().getModelManager().getAtlasTexture(new ResourceLocation("textures/atlas/blocks.png")).getSprite(location);
    }

    private static void drawColoredSpriteTiled(TextureAtlasSprite sprite, double x, double y, Color color, int width, int height) {
        int spriteWidth = sprite.getWidth();
        int spriteHeight = sprite.getHeight();
        int fullHeights = Math.floorDiv(height, spriteHeight);
        int fullWidths = Math.floorDiv(width, spriteWidth);

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        double spriteX = x - spriteWidth;
        double spriteY = y - spriteHeight;

        // Draw full sized sprites
        for (int i = 0; i < fullHeights; i++) {
            for (int j = 0; j < fullWidths; j++) {
                spriteX = x + j * spriteWidth;
                spriteY = y + i * spriteHeight;
                spriteVertex(spriteX, spriteY, spriteWidth, spriteHeight, minU, maxU, minV, maxV, red, green, blue, alpha);
            }
        }

        // Draw leftovers on the bottom
        int partialSpriteHeight = height - (fullHeights * spriteHeight);

        float partialMaxV = sprite.getInterpolatedV(partialSpriteHeight);

        for (int i = 0; i < fullWidths; i++) {
            spriteX = x + i * spriteWidth;
            spriteVertex(spriteX, spriteY + spriteHeight, spriteWidth, partialSpriteHeight, minU, maxU, minV, partialMaxV, red, green, blue, alpha);
        }

        // Draw leftovers on the left
        int partialSpriteWidth = width - (fullWidths * spriteWidth);

        float partialMaxU = sprite.getInterpolatedU(partialSpriteWidth);

        for (int i = 0; i < fullHeights; i++) {
            spriteY = y + i * spriteHeight;
            spriteVertex(spriteX + spriteWidth, spriteY, partialSpriteWidth, spriteHeight, minU, partialMaxU, minV, maxV, red, green, blue, alpha);
        }

        // Draw corner leftover
        spriteVertex(spriteX + spriteWidth, spriteY + spriteHeight, partialSpriteWidth, partialSpriteHeight, minU, partialMaxU, minV, partialMaxV, red, green, blue, alpha);
    }

    private static void spriteVertex(double x, double y, int width, int height, float minU, float maxU, float minV, float maxV, int red, int green, int blue, int alpha) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
        buffer.pos(x, y + height, 5).color(red, green, blue, alpha).tex(minU, maxV).endVertex();
        buffer.pos(x + width, y + height, 5).color(red, green, blue, alpha).tex(maxU, maxV).endVertex();
        buffer.pos(x + width, y , 5).color(red, green, blue, alpha).tex(maxU, minV).endVertex();
        buffer.pos(x, y, 5).color(red, green, blue, alpha).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }
}
