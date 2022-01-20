package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.tile.LiquidTankTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class LiquidStorageTileEntityRenderer extends TileEntityRenderer<LiquidTankTile> {

    public LiquidStorageTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(LiquidTankTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        FluidStack fluid = tile.getTankContents();

        if (!fluid.isEmpty()) {
            stack.push();
            stack.translate(.25, .0625, .25);

            Function<ResourceLocation, TextureAtlasSprite> sprites = Minecraft.getInstance().getAtlasSpriteGetter(RenderUtils.BLOCK_ATLAS_LOC);
            FluidAttributes attributes = fluid.getFluid().getAttributes();

            TextureAtlasSprite stillSprite = sprites.apply(attributes.getStillTexture(fluid));
            TextureAtlasSprite flowingSprite = sprites.apply(attributes.getFlowingTexture(fluid));

            IVertexBuilder builder = buf.getBuffer(RenderType.getSolid());
            Matrix4f matrix = stack.getLast().getMatrix();
            Matrix3f normal = stack.getLast().getNormal();

            float p = tile.getFillPercentage();
            float height = 12 * p;
            float maxSideV = flowingSprite.getInterpolatedV(16 * p);
            int color = attributes.getColor(fluid);
            int alpha = (color >> 24) & 0xFF;
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = color & 0xFF;

            renderSides(builder, matrix, normal, height, flowingSprite.getMinU(), flowingSprite.getMaxU(), flowingSprite.getMinV(), maxSideV, combinedLight, red, green, blue, alpha);

            if (height < 12) {
                renderTop(builder, matrix, normal, height, stillSprite.getMinU(), stillSprite.getMaxU(), stillSprite.getMinV(), stillSprite.getMaxV(), combinedLight, red, green, blue, alpha);
            }

            stack.pop();
        }
    }

    private void renderTop(IVertexBuilder builder, Matrix4f matrix, Matrix3f normal, float height, float minU, float maxU, float minV, float maxV, int light, int red, int green, int blue, int alpha) {
        float h = height / 16;

        builder.pos(matrix, 0, h, .5f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, h, .5f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, h, 0)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, 0, h, 0)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();
    }

    private void renderSides(IVertexBuilder builder, Matrix4f matrix, Matrix3f normal, float height, float minU, float maxU, float minV, float maxV, int light, int red, int green, int blue, int alpha) {
        float h = height / 16;

        builder.pos(matrix, 0, h, .0001f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, h, .0001f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, 0, .0001f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, 0, 0, .0001f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        // -------------------------------------
        builder.pos(matrix, 0, h, .4999f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, 0, 0, .4999f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, 0, .4999f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, h, .4999f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        //----------------------------------------
        builder.pos(matrix, .0001f, h, 0)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .0001f, 0, 0)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .0001f, 0, .5f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .0001f, h, .5f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        // ---------------east-----------------

        builder.pos(matrix, .4999f, h, 0)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .4999f, h, .5f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .4999f, 0, .5f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .4999f, 0, 0)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();
    }
}
