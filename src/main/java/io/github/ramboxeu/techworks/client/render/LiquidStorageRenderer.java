package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.component.LiquidStorageComponent;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.tile.LiquidTankTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class LiquidStorageRenderer {

    private static void render(FluidStack fluid, float fillPercentage, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight) {
        stack.push();
        stack.translate(.25, .0625, .25);

        Function<ResourceLocation, TextureAtlasSprite> sprites = Minecraft.getInstance().getAtlasSpriteGetter(RenderUtils.BLOCK_ATLAS_LOC);
        FluidAttributes attributes = fluid.getFluid().getAttributes();

        TextureAtlasSprite stillSprite = sprites.apply(attributes.getStillTexture(fluid));
        TextureAtlasSprite flowingSprite = sprites.apply(attributes.getFlowingTexture(fluid));

        IVertexBuilder builder = buf.getBuffer(RenderType.getSolid());
        Matrix4f matrix = stack.getLast().getMatrix();
        Matrix3f normal = stack.getLast().getNormal();

        float height = (12 * fillPercentage) / 16;
        float maxSideV = flowingSprite.getInterpolatedV(16 * fillPercentage);
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

    private static void renderTop(IVertexBuilder builder, Matrix4f matrix, Matrix3f normal, float height, float minU, float maxU, float minV, float maxV, int light, int red, int green, int blue, int alpha) {
        builder.pos(matrix, 0, height, .5f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, height, .5f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, height, 0)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, 0, height, 0)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();
    }

    private static void renderSides(IVertexBuilder builder, Matrix4f matrix, Matrix3f normal, float height, float minU, float maxU, float minV, float maxV, int light, int red, int green, int blue, int alpha) {
        builder.pos(matrix, 0, height, .0001f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .5f, height, .0001f)
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

        builder.pos(matrix, 0, height, .4999f)
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

        builder.pos(matrix, .5f, height, .4999f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .0001f, height, 0)
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

        builder.pos(matrix, .0001f, height, .5f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .4999f, height, 0)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 0)
                .endVertex();

        builder.pos(matrix, .4999f, height, .5f)
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

    public static Function<TileEntityRendererDispatcher, TileRenderer> tileRenderer() {
        return TileRenderer::new;
    }

    public static ItemStackRenderer stackRenderer() {
        return new ItemStackRenderer();
    }

    public static class ItemStackRenderer extends ItemStackTileEntityRenderer {

        private static final ModelResourceLocation BLOCK_MODEL = new ModelResourceLocation(TechworksBlocks.LIQUID_TANK.getId(), "");

        @Override
        public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
            Minecraft minecraft = Minecraft.getInstance();
            IBakedModel blockModel = minecraft.getModelManager().getModel(BLOCK_MODEL);
            RenderType type = RenderTypeLookup.func_239219_a_(stack, true);
            IVertexBuilder builder = ItemRenderer.getEntityGlintVertexBuilder(buf, type, true, stack.hasEffect());

            matrixStack.push();
            minecraft.getItemRenderer().renderModel(blockModel, stack, combinedLight, combinedOverlay, matrixStack, builder);
            matrixStack.pop();

            CompoundNBT tag = stack.getTag();
            if (tag != null && tag.contains("Tile", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT tileTag = tag.getCompound("Tile");
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tileTag.getCompound("Tank").getCompound("Fluid"));

                if (!fluid.isEmpty()) {
                    LiquidStorageComponent component = NBTUtils.deserializeComponent(tileTag, "Component");
                    float fillPercentage = (float) fluid.getAmount() / component.getStorageCapacity();

                    LiquidStorageRenderer.render(fluid, fillPercentage, matrixStack, buf, combinedLight);
                }
            }
        }
    }

    public static class TileRenderer extends TileEntityRenderer<LiquidTankTile> {

        public TileRenderer(TileEntityRendererDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public void render(LiquidTankTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
            FluidStack fluid = tile.getTankContents();
            float fillPercentage = tile.getFillPercentage();

            if (!fluid.isEmpty()) {
                LiquidStorageRenderer.render(fluid, fillPercentage, stack, buf, combinedLight);
            }
        }
    }
}
