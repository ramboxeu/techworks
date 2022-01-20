package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.util.RenderUtils;
import io.github.ramboxeu.techworks.common.component.EnergyStorageComponent;
import io.github.ramboxeu.techworks.common.registration.TechworksBlocks;
import io.github.ramboxeu.techworks.common.tile.EnergyStorageTile;
import io.github.ramboxeu.techworks.common.util.NBTUtils;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;

import java.util.function.Function;

public class EnergyStorageRenderer {
    private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "block/energy_storage");

    private static void render(IWorld world, BlockPos pos, Function<Direction, StorageMode> modes, float fillPercentage, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight) {
        Function<ResourceLocation, TextureAtlasSprite> sprites = Minecraft.getInstance().getAtlasSpriteGetter(RenderUtils.BLOCK_ATLAS_LOC);
        IVertexBuilder builder = buf.getBuffer(RenderType.getSolid());
        TextureAtlasSprite sprite = sprites.apply(TEX);

        float length = ((8 * fillPercentage) + 4) / 16;
        float maxEnergyBarU = sprite.getInterpolatedU(8);
        float minEnergyBarV = sprite.getInterpolatedV(8);
        float maxEnergyBarV = sprite.getInterpolatedV((8 * fillPercentage) + 8);

        for (Direction dir : Direction.values()) {
            float shade = world != null ? world.func_230487_a_(dir, true) : 1;
            int light = world != null ? WorldRenderer.getCombinedLight(world, pos.offset(dir)) : combinedLight;
            int red = (int) (shade * 255);
            int green = (int) (shade * 255);
            int blue = (int) (shade * 255);
            int alpha = 255;

            float minPortU = sprite.getInterpolatedU(12);
            float portOffset = computePortOffset(modes.apply(dir));
            float minPortV = sprite.getInterpolatedV(portOffset);
            float maxPortV = sprite.getInterpolatedV(portOffset + 4);

            stack.push();
            stack.translate(.5, .5, .5);
            stack.rotate(rotation(dir));
            stack.translate(-.5, -.5, -.5);

            Matrix4f matrix = stack.getLast().getMatrix();
            Matrix3f normal = stack.getLast().getNormal();

            renderSide(builder, matrix, normal, length, minPortU, sprite.getMaxU(), minPortV, maxPortV, sprite.getMinU(), maxEnergyBarU, minEnergyBarV, maxEnergyBarV, red, green, blue, alpha, light);

            stack.pop();
        }
    }

    private static float computePortOffset(StorageMode mode) {
        switch (mode) {
            case NONE:
                return 12;
            case INPUT:
                return 4;
            case OUTPUT:
                return 0;
            case BOTH:
                return 8;
            default:
                throw new AssertionError();
        }
    }

    private static Quaternion rotation(Direction dir) {
        switch (dir) {
            case UP:
                return new Quaternion(90, 0, 0, true);
            case DOWN:
                return new Quaternion(-90, 0, 0, true);
            case NORTH:
                return Quaternion.ONE;
            case SOUTH:
                return new Quaternion(0, 180, 0, true);
            case EAST:
                return new Quaternion(0, 90, 0, true);
            case WEST:
                return new Quaternion(0, -90, 0, true);
            default:
                throw new AssertionError();
        }
    }

    private static void renderSide(IVertexBuilder builder, Matrix4f matrix, Matrix3f normal, float length, float portMinU, float portMaxU, float portMinV, float portMaxV, float barMinU, float barMaxU, float barMinV, float barMaxV, int red, int blue, int green, int alpha, int light) {
        builder.pos(matrix, .25f, 1, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .25f, .75f, -.0001f)
                .color(red, green,blue , alpha)
                .tex(portMaxU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 0, .75f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 0, 1, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 1, 1, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 1, .75f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .75f, .75f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .75f, 1, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 1, .25f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 1, 0, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .75f, 0, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .75f, .25f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .25f, .25f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .25f, 0, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMaxU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 0, 0, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, 0, .25f, -.0001f)
                .color(red, green, blue, alpha)
                .tex(portMinU, portMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .75f, length, .2499f)
                .color(red, green, blue, alpha)
                .tex(barMaxU, barMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .75f, .25f, .2499f)
                .color(red, green, blue, alpha)
                .tex(barMaxU, barMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .25f, .25f, .2499f)
                .color(red, green, blue, alpha)
                .tex(barMinU, barMinV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();

        builder.pos(matrix, .25f, length, .2499f)
                .color(red, green, blue, alpha)
                .tex(barMinU, barMaxV)
                .lightmap(light)
                .normal(normal, 0, 0, -1)
                .endVertex();
    }

    public static Function<TileEntityRendererDispatcher, TileRenderer> tileRenderer() {
        return TileRenderer::new;
    }

    public static ItemStackRenderer stackRenderer() {
        return new ItemStackRenderer();
    }

    public static class ItemStackRenderer extends ItemStackTileEntityRenderer {

        private static final ModelResourceLocation BLOCK_MODEL = new ModelResourceLocation(TechworksBlocks.ENERGY_STORAGE.getId(), "");

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
                int energy = tileTag.getCompound("Battery").getInt("Energy");
                EnergyStorageComponent component = NBTUtils.deserializeComponent(tileTag, "Component");
                ListNBT sideConfig = tileTag.getList("SideConfig", Constants.NBT.TAG_STRING);

                if (sideConfig.isEmpty()) {
                    for (int i = 0; i < 6; i++) {
                        sideConfig.add(i, StringNBT.valueOf("BOTH"));
                    }
                }

                float fillPercentage = component != null ? (float) energy / component.getStorageCapacity() : 0;
                EnergyStorageRenderer.render(null, null, dir -> StorageMode.valueOf(sideConfig.getString(dir.getIndex())),  fillPercentage, matrixStack, buf, combinedLight);
            }
        }
    }

    public static class TileRenderer extends TileEntityRenderer<EnergyStorageTile> {

        public TileRenderer(TileEntityRendererDispatcher dispatcher) {
            super(dispatcher);
        }

        @Override
        public void render(EnergyStorageTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
            float percentage = tile.getFillPercentage();
            EnergyStorageRenderer.render(tile.getWorld(), tile.getPos(), tile::getSideStorageMode, percentage, stack, buf, combinedLight);
        }
    }
}
