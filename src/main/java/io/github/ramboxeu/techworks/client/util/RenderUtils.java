package io.github.ramboxeu.techworks.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

/*
* Some rendering tips:
*  1. Vertex placement:
*    Seems like vertexes need to be placed in a specific configuration to work properly.
*    This configuration is as follows: (minX, maxY) (left down), (maxX, maxY) (left up), (maxX, minY) (right up), (minX, minY) (right down)
*
*/

public class RenderUtils {
    public static final ResourceLocation BLOCK_ATLAS_LOC = PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    public static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("missingno");

    private static final Minecraft MC = Minecraft.getInstance();

    public static void drawFluid(MatrixStack stack, int x, int y, int width, int height, Color color, ResourceLocation stillTexture) {
        if (stillTexture == null) {
            stillTexture = new ResourceLocation("water_still");
        }

        TextureAtlasSprite sprite = bindBlockTexture(stillTexture);

        RenderSystem.disableBlend();
        drawTiledSprite(stack, sprite, x, y, 0, color, width, height);
        RenderSystem.enableBlend();
    }

    private static TextureAtlasSprite bindBlockTexture(ResourceLocation location) {
        MC.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        return MC.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);
    }

    private static void drawTiledSprite(MatrixStack stack, TextureAtlasSprite sprite, float x, float y, float z, Color color, int width, int height) {
        drawTiledTexture(stack, x, y, z, color, width, height, sprite.getWidth(), sprite.getHeight(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }

    // Null color means ignore it
    private static void drawTiledTexture(MatrixStack stack, float x, float y, float z, Color color, int width, int height, int textureWidth, int textureHeight, float minU, float maxU, float minV, float maxV) {
        int fullHeights = Math.floorDiv(height, textureHeight);
        int fullWidths = Math.floorDiv(width, textureWidth);

        float textureX = x - textureWidth;
        float textureY = y - textureHeight;

        int red = 255;
        int green = 255;
        int blue = 255;
        int alpha = 255;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        if (color == null) {
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        } else {
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
            red = color.getRed();
            green = color.getGreen();
            blue = color.getBlue();
            alpha = color.getAlpha();
        }

        // Draw full sized sprites
        for (int i = 0; i < fullHeights; i++) {
            for (int j = 0; j < fullWidths; j++) {
                textureX = x + j * textureWidth;
                textureY = y + i * textureHeight;
                buildVertex(buffer, stack.getLast().getMatrix(), textureX, textureY, z, textureWidth, textureHeight, minU, maxU, minV, maxV, red, green, blue, alpha);
            }
        }

        // Draw leftovers on the bottom
        int partialSpriteHeight = height - (fullHeights * textureHeight);
        float partialMaxV = getInterpolatedV(partialSpriteHeight, minV, maxV, textureHeight);

        if (partialSpriteHeight > 0) {
            for (int i = 0; i < fullWidths; i++) {
                textureX = x + i * textureWidth;
                buildVertex(buffer, stack.getLast().getMatrix(), textureX, textureY + textureHeight, z, textureWidth, partialSpriteHeight, minU, maxU, minV, partialMaxV, red, green, blue, alpha);
            }
        }

        // Draw leftovers on the left
        int partialSpriteWidth = width - (fullWidths * textureWidth);
        float partialMaxU = getInterpolatedU(partialSpriteWidth, minU, maxU, textureWidth);

        if (partialSpriteWidth > 0) {
            for (int i = 0; i < fullHeights; i++) {
                textureY = y + i * textureHeight;
                buildVertex(buffer, stack.getLast().getMatrix(), textureX + textureWidth, textureY, z, partialSpriteWidth, textureHeight, minU, partialMaxU, minV, maxV, red, green, blue, alpha);
            }
        }

        // Draw corner leftover
        if (partialSpriteHeight > 0 || partialSpriteWidth > 0) {
            buildVertex(buffer, stack.getLast().getMatrix(), textureX + textureWidth, textureY + textureHeight, z, partialSpriteWidth, partialSpriteHeight, minU, partialMaxU, minV, partialMaxV, red, green, blue, alpha);
        }

        tessellator.draw();
    }

    private static float getInterpolatedU(float u, float minU, float maxU, int textureWidth) {
        float width = maxU - minU;
        return minU + width * u / textureWidth;
    }

    private static float getInterpolatedV(float v, float minV, float maxV, int textureHeight) {
        float height = maxV - minV;
        return minV + height * v / textureHeight;
    }

    private static void buildVertex(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, float width, float height, float minU, float maxU, float minV, float maxV, int red, int green, int blue, int alpha) {
        buffer.pos(matrix, x, y + height, z).color(red, green, blue, alpha).tex(minU, maxV).endVertex();
        buffer.pos(matrix, x + width, y + height, z).color(red, green, blue, green).tex(maxU, maxV).endVertex();
        buffer.pos(matrix, x + width, y, z).color(red, green, blue, green).tex(maxU, minV).endVertex();
        buffer.pos(matrix, x, y, z).color(red, green, blue, green).tex(minU, minV).endVertex();
    }
}
