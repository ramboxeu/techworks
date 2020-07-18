package io.github.ramboxeu.techworks.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/*
* Some rendering tips:
*  1. Vertex placement:
*    Seems like vertexes need to be placed in a specific configuration to work properly.
*    This configuration is as follows: (minX, maxY), (maxX, maxY), (maxX, minY), (minX, minY)
*
*/

public class RenderUtils {

    private static final Minecraft MC = Minecraft.getInstance();

//    public static void drawFluidInTank(int tankX, int tankY, FluidStack fluid, int tankWidth, int tankHeight, int capacity) {
//        if (fluid == null || fluid.isEmpty())
//            return;
//
//        GL11.glPushMatrix();
//        ResourceLocation texture = fluid.getFluid().getAttributes().getStillTexture();
//        java.awt.Color color = new java.awt.Color(fluid.getFluid().getAttributes().getColor());
//
//        int height = (int) (((float) fluid.getAmount() / capacity) * tankHeight);
//        int y = (tankY + tankHeight) - height;
//
//        drawColoredSpriteTiled(findAndBindBlockTexture(texture), tankX, y, color, tankWidth, height);
//        GL11.glPopMatrix();
//    }

//    public static void drawGasInTank(int tankX, int tankY, int amount, Color color, int tankWidth, int tankHeight, int capacity) {
//        if (amount <= 0)
//            return;
//
//        GL11.glPushMatrix();
//
//        int height = (int) (((float) amount / capacity) * tankHeight);
//        int y = (tankY + tankHeight) - height;
//
//        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Techworks.MOD_ID, "textures/gas/gas.png"));
//
//        drawTiledTexture(tankX, y, color, tankWidth, height, 0,0, 16, 16);
//        GL11.glPopMatrix();
//    }
//
//    public static void drawEnergyInStorage(int tankX, int tankY, int amount, int tankWidth, int tankHeight, int capacity) {
//        if (amount <= 0)
//            return;
//
//        GL11.glPushMatrix();
//
//        int height = (int) (((float) amount / capacity) * tankHeight);
//        int y = (tankY + tankHeight) - height;
//
//        colorVertex(tankX, y, tankWidth, height, 181, 25, 27, 255);
//        GL11.glPopMatrix();
//    }

    public static void drawFluid(MatrixStack stack, int x, int y, int width, int height, Color color, ResourceLocation stillTexture) {
        if (stillTexture == null) {
            stillTexture = new ResourceLocation("water_still");
        }

        TextureAtlasSprite sprite = bindBlockTexture(stillTexture);

        drawTiledSprite(stack, sprite, x, y, 5, color, width, height);
    }

    private static TextureAtlasSprite bindBlockTexture(ResourceLocation location) {
        MC.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        return MC.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);
    }

    // Null color means ignore it
    private static void drawTiledSprite(MatrixStack stack, TextureAtlasSprite sprite, float x, float y, float z, Color color, int width, int height) {
        int spriteWidth = sprite.getWidth();
        int spriteHeight = sprite.getHeight();
        int fullHeights = Math.floorDiv(height, spriteHeight);
        int fullWidths = Math.floorDiv(width, spriteWidth);

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        float spriteX = x - spriteWidth;
        float spriteY = y - spriteHeight;

        int red = 255;
        int green = 255;
        int blue = 255;
        int alpha = 255;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

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
                spriteX = x + j * spriteWidth;
                spriteY = y + i * spriteHeight;
                buildVertex(buffer, stack.getLast().getMatrix(), spriteX, spriteY, z, spriteWidth, spriteHeight, minU, maxU, minV, maxV, red, green, blue, alpha);
            }
        }

        // Draw leftovers on the bottom
        int partialSpriteHeight = height - (fullHeights * spriteHeight);

        float partialMaxV = sprite.getInterpolatedV(partialSpriteHeight);

        for (int i = 0; i < fullWidths; i++) {
            spriteX = x + i * spriteWidth;
            buildVertex(buffer, stack.getLast().getMatrix(), spriteX, spriteY + spriteHeight, z, spriteWidth, partialSpriteHeight, minU, maxU, minV, partialMaxV, red, green, blue, alpha);
        }

        // Draw leftovers on the left
        int partialSpriteWidth = width - (fullWidths * spriteWidth);

        float partialMaxU = sprite.getInterpolatedU(partialSpriteWidth);

        for (int i = 0; i < fullHeights; i++) {
            spriteY = y + i * spriteHeight;
            buildVertex(buffer, stack.getLast().getMatrix(), spriteX + spriteWidth, spriteY, z, partialSpriteWidth, spriteHeight, minU, partialMaxU, minV, maxV, red, green, blue, alpha);
        }

        // Draw corner leftover
        buildVertex(buffer, stack.getLast().getMatrix(), spriteX + spriteWidth, spriteY + spriteHeight, z, partialSpriteWidth, partialSpriteHeight, minU, partialMaxU, minV, partialMaxV, red, green, blue, alpha);

        buffer.finishDrawing();
        WorldVertexBufferUploader.draw(buffer);
    }

    private static void drawTiledTexture(double x, double y, Color color, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight) {
        int fullHeights = Math.floorDiv(height, textureHeight);
        int fullWidths = Math.floorDiv(width, textureWidth);

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        double spriteX = x - textureWidth;
        double spriteY = y - textureHeight;

        int minU = textureWidth;
        int maxU = textureWidth + textureX;
        int minV = textureHeight;
        int maxV = textureHeight + textureY;

        // Draw full sized sprites
        for (int i = 0; i < fullHeights; i++) {
            for (int j = 0; j < fullWidths; j++) {
                spriteX = x + j * textureWidth;
                spriteY = y + i * textureHeight;
                spriteVertex(spriteX, spriteY, textureWidth, textureHeight, minU, maxU, minV, maxV, red, green, blue, alpha);
            }
        }

        // Draw leftovers on the bottom
        int partialSpriteHeight = height - (fullHeights * textureHeight);

        float partialMaxV = maxV - partialSpriteHeight;

        for (int i = 0; i < fullWidths; i++) {
            spriteX = x + i * textureWidth;
            spriteVertex(spriteX, spriteY + textureHeight, textureWidth, partialSpriteHeight, minU, maxU, minV, partialMaxV, red, green, blue, alpha);
        }

        // Draw leftovers on the left
        int partialSpriteWidth = width - (fullWidths * textureWidth);

        float partialMaxU = maxU - partialSpriteWidth;

        for (int i = 0; i < fullHeights; i++) {
            spriteY = y + i * textureHeight;
            spriteVertex(spriteX + textureWidth, spriteY, partialSpriteWidth, textureHeight, minU, partialMaxU, minV, maxV, red, green, blue, alpha);
        }

        // Draw corner leftover
        spriteVertex(spriteX + textureWidth, spriteY + textureHeight, partialSpriteWidth, partialSpriteHeight, minU, partialMaxU, minV, partialMaxV, red, green, blue, alpha);
    }

    private static void buildVertex(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, float width, float height, float minU, float maxU, float minV, float maxV, int red, int green, int blue, int alpha) {
        buffer.pos(matrix, x, y + height, z).color(red, green, blue, alpha).tex(minU, maxV).endVertex();
        buffer.pos(matrix, x + width, y + height, z).color(red, green, blue, green).tex(maxU, maxV).endVertex();
        buffer.pos(matrix, x + width, y, z).color(red, green, blue, green).tex(maxU, minV).endVertex();
        buffer.pos(matrix, x, y, z).color(red, green, blue, green).tex(minU, minV).endVertex();
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

    private static void colorVertex(double x, double y, int width, int height, int red, int green, int blue, int alpha) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x, y + height, 5).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, y + height, 5).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + width, y, 5).color(red, green, blue, alpha).endVertex();
        buffer.pos(x, y, 5).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
}
