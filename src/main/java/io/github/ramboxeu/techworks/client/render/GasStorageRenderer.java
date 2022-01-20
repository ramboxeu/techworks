package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.component.GasStorageComponent;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.tile.GasTankTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
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

public class GasStorageRenderer {

    private static void render(FluidStack fluid, float fillPercentage, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight) {
        stack.push();
        stack.translate(.1875, .1875, .1875);
        Function<ResourceLocation, TextureAtlasSprite> sprites = Minecraft.getInstance().getAtlasSpriteGetter(RenderUtils.BLOCK_ATLAS_LOC);
        FluidAttributes attributes = fluid.getFluid().getAttributes();

        TextureAtlasSprite flowingSprite = sprites.apply(attributes.getFlowingTexture(fluid));
        TextureAtlasSprite sprite = flowingSprite != null && flowingSprite.getName() != MissingTextureSprite.getLocation() ? flowingSprite : sprites.apply(attributes.getStillTexture(fluid));

        IVertexBuilder builder = buf.getBuffer(RenderType.getSolid());
        Matrix4f matrix = stack.getLast().getMatrix();
        Matrix3f normal = stack.getLast().getNormal();

        int color = attributes.getColor(fluid);
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        float maxV = sprite.getInterpolatedV(7 * fillPercentage);
        float height = (fillPercentage * 7) / 16;

        renderSides(builder, matrix, normal, height, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), maxV, combinedLight, red, green, blue, 255);
        stack.pop();
    }

    private static void renderSides(IVertexBuilder builder, Matrix4f matrix, Matrix3f normal, float height, float minU, float maxU, float minV, float maxV, int light, int red, int green, int blue, int alpha) {
        builder.pos(matrix, .4375f, height, -.0001f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .4375f, 0, -.0001f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .1875f, 0, -.0001f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .1875f, height, -.0001f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .4375f, height, .6251f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 1)
                .endVertex();

        builder.pos(matrix, .1875f, height, .6251f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 0, 0, 1)
                .endVertex();

        builder.pos(matrix, .1875f, 0, .6251f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 1)
                .endVertex();

        builder.pos(matrix, .4375f, 0, .6251f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 0, 0, 1)
                .endVertex();

        builder.pos(matrix, -.0001f, height, .4375f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, -1, 0, 0)
                .endVertex();

        builder.pos(matrix, -.0001f, height, .1875f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, -1, 0, 0)
                .endVertex();

        builder.pos(matrix, -.0001f, 0, .1875f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, -1, 0, 0)
                .endVertex();

        builder.pos(matrix, -.0001f, 0, .4375f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, -1, 0, 0)
                .endVertex();

        builder.pos(matrix, .6251f, height, .4375f)
                .color(red, green, blue, alpha)
                .tex(maxU, maxV)
                .lightmap(light)
                .normal(normal, 1, 0, 0)
                .endVertex();

        builder.pos(matrix, .6251f, 0, .4375f)
                .color(red, green, blue, alpha)
                .tex(maxU, minV)
                .lightmap(light)
                .normal(normal, 1, 0, 0)
                .endVertex();

        builder.pos(matrix, .6251f, 0, .1875f)
                .color(red, green, blue, alpha)
                .tex(minU, minV)
                .lightmap(light)
                .normal(normal, 1, 0, 0)
                .endVertex();

        builder.pos(matrix, .6251f, height, .1875f)
                .color(red, green, blue, alpha)
                .tex(minU, maxV)
                .lightmap(light)
                .normal(normal, 1, 0, 0)
                .endVertex();
    }

    public static Function<TileEntityRendererDispatcher, TileRenderer> tileRenderer() {
        return TileRenderer::new;
    }

    public static ItemStackRenderer stackRenderer() {
        return new ItemStackRenderer();
    }

    public static class ItemStackRenderer extends ItemStackTileEntityRenderer {

        private static final ModelResourceLocation BLOCK_MODEL = new ModelResourceLocation(TechworksBlocks.GAS_TANK.getId(), "");

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
                    GasStorageComponent component = NBTUtils.deserializeComponent(tileTag, "Component");
                    float fillPercentage = (float) fluid.getAmount() / component.getStorageCapacity();

                    GasStorageRenderer.render(fluid, fillPercentage, matrixStack, buf, combinedLight);
                }
            }
        }
    }

    public static class TileRenderer extends TileEntityRenderer<GasTankTile> {

        public TileRenderer(TileEntityRendererDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public void render(GasTankTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
            FluidStack fluid = tile.getTankContents();
            float percentage = tile.getFillPercentage();

            if (!fluid.isEmpty()) {
                GasStorageRenderer.render(fluid, percentage, stack, buf, combinedLight);
            }
        }
    }
}
