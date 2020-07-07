package io.github.ramboxeu.techworks.client.model;

import com.google.common.collect.ImmutableList;
import io.github.ramboxeu.techworks.common.model.TechworksModelData;
import io.github.ramboxeu.techworks.common.util.CableConnections;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;

import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CableBakedModel implements IDynamicBakedModel {
    private TextureAtlasSprite sprite;

    public CableBakedModel(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        ArrayList<BakedQuad> quads = new ArrayList<>();

//        double o = .35;
//
//        CableConnections connections = data.getData(TechworksModelData.PIPE_CONNECTIONS);
//
//        if (connections == null) {
//            connections = new CableConnections.Builder().build();
//        }
//
//        if (connections.isUp()) {
//            quads.add(createQuad(new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, 1, o), new Vec3d(1 - o, 1, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), Direction.EAST ));
//            quads.add(createQuad(new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, 1, 1 - o), new Vec3d(o, 1, o), new Vec3d(o, 1 - o, o), Direction.WEST));
//            quads.add(createQuad(new Vec3d(o, 1, o), new Vec3d(1 - o, 1, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(o, 1 - o, o), Direction.NORTH));
//            quads.add(createQuad(new Vec3d(o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, 1, 1 - o), new Vec3d(o, 1, 1 - o), Direction.SOUTH));
//        } else {
//            quads.add(createQuad(new Vec3d(o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, o), new Vec3d(o, 1 - o, o), Direction.UP));
//        }
//
//        if (connections.isDown()) {
//            quads.add(createQuad(new Vec3d(1 - o, 0, o), new Vec3d(1 - o, o, o), new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, 0, 1 - o), Direction.EAST));
//            quads.add(createQuad(new Vec3d(o, 0, 1 - o), new Vec3d(o, o, 1 - o), new Vec3d(o, o, o), new Vec3d(o, 0, o), Direction.WEST));
//            quads.add(createQuad(new Vec3d(o, o, o), new Vec3d(1 - o, o, o), new Vec3d(1 - o, 0, o), new Vec3d(o, 0, o), Direction.NORTH));
//            quads.add(createQuad(new Vec3d(o, 0, 1 - o), new Vec3d(1 - o, 0, 1 - o), new Vec3d(1 - o, o, 1 - o), new Vec3d(o, o, 1 - o), Direction.SOUTH));
//        } else {
//            quads.add(createQuad(new Vec3d(o, o, o), new Vec3d(1 - o, o, o), new Vec3d(1 - o, o, 1 - o), new Vec3d(o, o, 1 - o), Direction.DOWN));
//        }
//
//        if (connections.isEast()) {
//            quads.add(createQuad(new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1, 1 - o, 1 - o), new Vec3d(1, 1 - o, o), new Vec3d(1 - o, 1 - o, o), Direction.UP));
//            quads.add(createQuad(new Vec3d(1 - o, o, o), new Vec3d(1, o, o), new Vec3d(1, o, 1 - o), new Vec3d(1 - o, o, 1 - o), Direction.DOWN));
//            quads.add(createQuad(new Vec3d(1 - o, 1 - o, o), new Vec3d(1, 1 - o, o), new Vec3d(1, o, o), new Vec3d(1 - o, o, o), Direction.SOUTH));
//            quads.add(createQuad(new Vec3d(1 - o, o, 1 - o), new Vec3d(1, o, 1 - o), new Vec3d(1, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), Direction.NORTH));
//        } else {
//            quads.add(createQuad(new Vec3d(1 - o, o, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, o, 1 - o), Direction.EAST));
//        }
//
//        if (connections.isWest()) {
//            quads.add(createQuad(new Vec3d(0, 1 - o, 1 - o), new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, 1 - o, o), new Vec3d(0, 1 - o, o), Direction.UP));
//            quads.add(createQuad(new Vec3d(0, o, o), new Vec3d(o, o, o), new Vec3d(o, o, 1 - o), new Vec3d(0, o, 1 - o), Direction.DOWN));
//            quads.add(createQuad(new Vec3d(0, 1 - o, o), new Vec3d(o, 1 - o, o), new Vec3d(o, o, o), new Vec3d(0, o, o), Direction.SOUTH));
//            quads.add(createQuad(new Vec3d(0, o, 1 - o), new Vec3d(o, o, 1 - o), new Vec3d(o, 1 - o, 1 - o), new Vec3d(0, 1 - o, 1 - o), Direction.NORTH));
//        } else {
//            quads.add(createQuad(new Vec3d(o, o, 1 - o), new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, 1 - o, o), new Vec3d(o, o, o), Direction.WEST));
//        }
//
//        if (connections.isNorth()) {
//            quads.add(createQuad(new Vec3d(o, 1 - o, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, 1 - o, 0), new Vec3d(o, 1 - o, 0), Direction.UP));
//            quads.add(createQuad(new Vec3d(o, o, 0), new Vec3d(1 - o, o, 0), new Vec3d(1 - o, o, o), new Vec3d(o, o, o), Direction.DOWN));
//            quads.add(createQuad(new Vec3d(1 - o, o, 0), new Vec3d(1 - o, 1 - o, 0), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, o, o), Direction.EAST));
//            quads.add(createQuad(new Vec3d(o, o, o), new Vec3d(o, 1 - o, o), new Vec3d(o, 1 - o, 0), new Vec3d(o, o, 0), Direction.WEST));
//        } else {
//            quads.add(createQuad(new Vec3d(o, 1 - o, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, o, o), new Vec3d(o, o, o), Direction.NORTH));
//        }
//        if (connections.isSouth()) {
//            quads.add(createQuad(new Vec3d(o, 1 - o, 1), new Vec3d(1 - o, 1 - o, 1), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(o, 1 - o, 1 - o), Direction.UP));
//            quads.add(createQuad(new Vec3d(o, o, 1 - o), new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, o, 1), new Vec3d(o, o, 1), Direction.DOWN));
//            quads.add(createQuad(new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1), new Vec3d(1 - o, o, 1), Direction.EAST));
//            quads.add(createQuad(new Vec3d(o, o, 1), new Vec3d(o, 1 - o, 1), new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, o, 1 - o), Direction.WEST));
//        } else {
//            quads.add(createQuad(new Vec3d(o, o, 1 - o), new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(o, 1 - o, 1 - o), Direction.SOUTH));
//        }

        return quads;
    }

//    private void putVertex(BakedQuadBuilder builder, Vec3d normal, double x, double y, double z, float u, float v) {
//        ImmutableList<VertexFormatElement> format = DefaultVertexFormats.BLOCK.getElements();
//        for (int e = 0; e < format.size(); e++) {
//            switch (format.get(e).getUsage()) {
//                case POSITION:
//                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
//                    break;
//                case COLOR:
//                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
//                    break;
//                case UV:
//                    if (format.get(e).getIndex() == 0) {
//                        u = sprite.getInterpolatedU(u);
//                        v = sprite.getInterpolatedV(v);
//                        builder.put(e, u, v, 0f, 1f);
//                        break;
//                    }
//                case NORMAL:
//                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
//                    break;
//                default:
//                    builder.put(e);
//                    break;
//            }
//        }
//    }
//
//    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, Direction orientation) {
//        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();
//
//        BakedQuadBuilder builder = new BakedQuadBuilder();
//        builder.setTexture(sprite);
//        builder.setQuadOrientation(orientation);
//        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0);
//        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0);
//        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16);
//        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16);
//        return builder.build();
//    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return sprite;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}
