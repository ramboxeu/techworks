package io.github.ramboxeu.techworks.client.model.cable;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.util.TextureCoords;
import io.github.ramboxeu.techworks.client.util.TextureCoordsGroup;
import io.github.ramboxeu.techworks.common.tile.BaseCableTile;
import io.github.ramboxeu.techworks.common.util.DirectionUtils;
import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnection;
import io.github.ramboxeu.techworks.common.util.cable.connection.CableConnections;
import io.github.ramboxeu.techworks.common.util.model.Cuboid;
import io.github.ramboxeu.techworks.common.util.model.CuboidGroup;
import io.github.ramboxeu.techworks.common.util.model.Face;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CableBakedModel implements IDynamicBakedModel {
    private final TextureAtlasSprite baseTexture;
    private final TextureAtlasSprite particleTexture;
    private final TextureAtlasSprite connectorTexture;

    public CableBakedModel(TextureAtlasSprite baseTexture, TextureAtlasSprite particleTexture, TextureAtlasSprite connectorTexture) {
        this.baseTexture = baseTexture;
        this.particleTexture = particleTexture;
        this.connectorTexture = connectorTexture;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData data) {
        CableConnections connections = data.getData(BaseCableTile.CONNECTIONS);

        if (connections == null) {
            connections = CableConnections.DEFAULT;
        }

        ArrayList<BakedQuad> quads = new ArrayList<>();

        connection(quads, Direction.UP, connections);
        connection(quads, Direction.DOWN, connections);
        connection(quads, Direction.EAST, connections);
        connection(quads, Direction.WEST, connections);
        connection(quads, Direction.NORTH, connections);
        connection(quads, Direction.SOUTH, connections);

        return quads;
    }

    private void connection(List<BakedQuad> quads, Direction side, CableConnections connections) {
        CuboidGroup cuboid = CableModel.getCuboidGroup(side);
        TextureCoordsGroup tex = CableModel.getTextureCoords(side);
        CableConnection connection = connections.getConnection(side);

        if (connection.getStatus().isConnected()) {
            if (connection.getMode().isConnection()) {
                Cuboid conn = cuboid.getConnection();
                Direction[] directions = directions(side);
                TextureCoordsGroup.Connection group = tex.getConnection();

                quads.add(quad(conn, group.vertical, group.horizontal, directions[0], side, baseTexture, false));
                quads.add(quad(conn, group.vertical, group.horizontal, directions[1], side, baseTexture, false));
                quads.add(quad(conn, group.vertical, group.horizontal, directions[2], side, baseTexture, false));
                quads.add(quad(conn, group.vertical, group.horizontal, directions[3], side, baseTexture, false));
            } else {
                Cuboid connector = cuboid.getConnector();
                Cuboid connected = cuboid.getConnected();
                Direction[] connectorDirs = directions(side);
                Direction[] connectedDirs = directions(side);
                TextureCoordsGroup.Connected connectedGroup = tex.getConnected();
                TextureCoordsGroup.Connector connectorGroup = tex.getConnector();

                TextureCoords vertical;
                TextureCoords horizontal;

                if (connection.getMode().isInput()) {
                    vertical = connectorGroup.inputVertical;
                    horizontal = connectorGroup.inputHorizontal;
                } else if (connection.getMode().isOutput()) {
                    vertical = connectorGroup.outputVertical;
                    horizontal = connectorGroup.outputHorizontal;
                } else {
                    vertical = connectorGroup.vertical;
                    horizontal = connectorGroup.horizontal;
                }

                quads.add(quad(connector, connectorGroup.back, null, side, side, connectorTexture, true));
                quads.add(quad(connector, vertical, horizontal, connectorDirs[0], side, connectorTexture, true));
                quads.add(quad(connector, vertical, horizontal, connectorDirs[1], side, connectorTexture, true));
                quads.add(quad(connector, vertical, horizontal, connectorDirs[2], side, connectorTexture, true));
                quads.add(quad(connector, vertical, horizontal, connectorDirs[3], side, connectorTexture, true));

                quads.add(quad(connected, connectedGroup.vertical, connectedGroup.horizontal, connectedDirs[0], side, baseTexture, false));
                quads.add(quad(connected, connectedGroup.vertical, connectedGroup.horizontal, connectedDirs[1], side, baseTexture, false));
                quads.add(quad(connected, connectedGroup.vertical, connectedGroup.horizontal, connectedDirs[2], side, baseTexture, false));
                quads.add(quad(connected, connectedGroup.vertical, connectedGroup.horizontal, connectedDirs[3], side, baseTexture, false));
            }
        } else {
            TextureCoords coords = tex.getDisconnected().chooseCoords(connections.getConnection(side));
            Face face = cuboid.getConnection().getFace(side);

            Vector3d vec1 = face.leftDown;
            Vector3d vec2;
            Vector3d vec3 = face.rightUp;
            Vector3d vec4;

            if (DirectionUtils.isHorizontal(side)) {
                if (DirectionUtils.isPositive(side)) {
                    vec2 = face.rightDown;
                    vec4 = face.leftUp;
                } else {
                    vec2 = face.leftUp;
                    vec4 = face.rightDown;
                }
            } else {
                if (DirectionUtils.isNegative(side)) {
                    vec2 = face.rightDown;
                    vec4 = face.leftUp;
                } else {
                    vec2 = face.leftUp;
                    vec4 = face.rightDown;
                }
            }

            Vector2f tex1 = new Vector2f(coords.minU, coords.minV);
            Vector2f tex2 = new Vector2f(coords.minU, coords.maxV);
            Vector2f tex3 = new Vector2f(coords.maxU, coords.maxV);
            Vector2f tex4 = new Vector2f(coords.maxU, coords.minV);

            quads.add(quad(vec1, vec2, vec3, vec4, tex1, tex2, tex3, tex4, side, baseTexture));
        }
    }

    private Direction[] directions(Direction side) {
        Direction[] directions = new Direction[4];

        int i = 0;

        for (Direction direction : Direction.values()) {
            if (side == direction) continue;
            if (side.getOpposite() == direction) continue;
            if (i >= 4) break;

            directions[i++] = direction;
        }

        return directions;
    }

    private BakedQuad quad(Cuboid cuboid, TextureCoords vertical, TextureCoords horizontal, Direction direction, Direction side, TextureAtlasSprite sprite, boolean flag) {
        TextureCoords coords;
        Face face = cuboid.getFace(direction);

        if (horizontal == null) {
            coords = vertical;
        } else {
            if (DirectionUtils.isVertical(side)) {
                if (DirectionUtils.isXAxis(direction)) {
                    coords = vertical;
                } else {
                    coords = horizontal;
                }
            } else {
                if (DirectionUtils.isHorizontal(side)) {
                    coords = horizontal;
                } else {
                    coords = vertical;
                }
            }
        }

        Vector3d vec2;
        Vector3d vec4;

        if (DirectionUtils.isVertical(direction)) {
            if (DirectionUtils.isPositive(direction)) {
                vec2 = face.rightDown;
                vec4 = face.leftUp;
            } else {
                vec2 = face.leftUp;
                vec4 = face.rightDown;
            }
        } else {
            if (DirectionUtils.isNegative(direction)) {
                vec2 = face.rightDown;
                vec4 = face.leftUp;
            } else {
                vec2 = face.leftUp;
                vec4 = face.rightDown;
            }
        }

        Vector2f tex1;
        Vector2f tex2;
        Vector2f tex3;
        Vector2f tex4;

        if (DirectionUtils.isXAxis(side)) {
            if (direction == Direction.DOWN || direction == Direction.SOUTH) {
                tex4 = new Vector2f(coords.minU, coords.minV);
                tex1 = new Vector2f(coords.minU, coords.maxV);
                tex2 = new Vector2f(coords.maxU, coords.maxV);
                tex3 = new Vector2f(coords.maxU, coords.minV);
            } else {
                tex1 = new Vector2f(coords.minU, coords.minV);
                tex2 = new Vector2f(coords.minU, coords.maxV);
                tex3 = new Vector2f(coords.maxU, coords.maxV);
                tex4 = new Vector2f(coords.maxU, coords.minV);
            }
        } else if (DirectionUtils.isZAxis(side)) {
            if (direction == Direction.WEST || direction == Direction.UP) {
                tex4 = new Vector2f(coords.minU, coords.minV);
                tex1 = new Vector2f(coords.minU, coords.maxV);
                tex2 = new Vector2f(coords.maxU, coords.maxV);
                tex3 = new Vector2f(coords.maxU, coords.minV);
            } else {
                tex1 = new Vector2f(coords.minU, coords.minV);
                tex2 = new Vector2f(coords.minU, coords.maxV);
                tex3 = new Vector2f(coords.maxU, coords.maxV);
                tex4 = new Vector2f(coords.maxU, coords.minV);
            }
        } else {
            if (direction == Direction.WEST || direction == Direction.SOUTH) {
                tex4 = new Vector2f(coords.minU, coords.minV);
                tex1 = new Vector2f(coords.minU, coords.maxV);
                tex2 = new Vector2f(coords.maxU, coords.maxV);
                tex3 = new Vector2f(coords.maxU, coords.minV);
            } else {
                tex1 = new Vector2f(coords.minU, coords.minV);
                tex2 = new Vector2f(coords.minU, coords.maxV);
                tex3 = new Vector2f(coords.maxU, coords.maxV);
                tex4 = new Vector2f(coords.maxU, coords.minV);
            }
        }

        if (flag) {
            if (side == Direction.UP) {
                if (direction == Direction.NORTH) {
                    tex3 = new Vector2f(coords.minU, coords.minV);
                    tex4 = new Vector2f(coords.minU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.maxV);
                    tex2 = new Vector2f(coords.maxU, coords.minV);
                }

                if (direction == Direction.WEST) {
                    tex2 = new Vector2f(coords.minU, coords.minV);
                    tex3 = new Vector2f(coords.minU, coords.maxV);
                    tex4 = new Vector2f(coords.maxU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.minV);
                }
            }

            if (side == Direction.DOWN) {
                if (direction == Direction.EAST) {
                    tex3 = new Vector2f(coords.minU, coords.minV);
                    tex4 = new Vector2f(coords.minU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.maxV);
                    tex2 = new Vector2f(coords.maxU, coords.minV);
                }

                if (direction == Direction.SOUTH) {
                    tex2 = new Vector2f(coords.minU, coords.minV);
                    tex3 = new Vector2f(coords.minU, coords.maxV);
                    tex4 = new Vector2f(coords.maxU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.minV);
                }
            }

            if (side == Direction.WEST) {
                if (direction == Direction.SOUTH || direction == Direction.DOWN) {
                    tex2 = new Vector2f(coords.minU, coords.minV);
                    tex3 = new Vector2f(coords.minU, coords.maxV);
                    tex4 = new Vector2f(coords.maxU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.minV);
                }

                if (direction == Direction.NORTH || direction == Direction.UP) {
                    tex3 = new Vector2f(coords.minU, coords.minV);
                    tex4 = new Vector2f(coords.minU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.maxV);
                    tex2 = new Vector2f(coords.maxU, coords.minV);
                }
            }

            if (side == Direction.SOUTH) {
                if (direction == Direction.WEST || direction == Direction.UP) {
                    tex2 = new Vector2f(coords.minU, coords.minV);
                    tex3 = new Vector2f(coords.minU, coords.maxV);
                    tex4 = new Vector2f(coords.maxU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.minV);
                }

                if (direction == Direction.EAST || direction == Direction.DOWN) {
                    tex3 = new Vector2f(coords.minU, coords.minV);
                    tex4 = new Vector2f(coords.minU, coords.maxV);
                    tex1 = new Vector2f(coords.maxU, coords.maxV);
                    tex2 = new Vector2f(coords.maxU, coords.minV);
                }
            }
        }

        return quad(face.leftDown, vec2, face.rightUp, vec4, tex1, tex2, tex3, tex4, direction, sprite);
    }

    private BakedQuad quad(Vector3d vec1, Vector3d vec2, Vector3d vec3, Vector3d vec4, Vector2f tex1, Vector2f tex2, Vector2f tex3, Vector2f tex4, Direction direction, TextureAtlasSprite sprite) {
        Vector3d normal = vec2.subtract(vec3).crossProduct(vec4.subtract(vec3)).normalize();

        BakedQuadBuilder builder = new BakedQuadBuilder();
        builder.setQuadOrientation(direction);
        builder.setTexture(baseTexture);
        vertex(builder, normal.x, normal.y, normal.z, vec1.getX(), vec1.getY(), vec1.getZ(), tex1.x, tex1.y, sprite);
        vertex(builder, normal.x, normal.y, normal.z, vec2.getX(), vec2.getY(), vec2.getZ(), tex2.x, tex2.y, sprite);
        vertex(builder, normal.x, normal.y, normal.z, vec3.getX(), vec3.getY(), vec3.getZ(), tex3.x, tex3.y, sprite);
        vertex(builder, normal.x, normal.y, normal.z, vec4.getX(), vec4.getY(), vec4.getZ(), tex4.x, tex4.y, sprite);
        return builder.build();
    }

    private void vertex(BakedQuadBuilder builder, double normalX, double normalY, double normalZ, double x, double y, double z, float u, float v, TextureAtlasSprite sprite) {
        builder.put(0, (float) x, (float) y, (float) z, 1.0f); // POSITION
        builder.put(1, 1.0f, 1.0f, 1.0f, 1.0f); // COLOR
        builder.put(2, getInterpolatedU(u, sprite), getInterpolatedV(v, sprite), 0f, 1.0f); // UV (TEX_2F)
        builder.put(3); // UV (TEX_2SB)
        builder.put(4, (float) normalX, (float) normalY, (float) normalZ); // NORMAL
        builder.put(5); // PADDING
    }

    private float getInterpolatedU(float u, TextureAtlasSprite sprite) {
        return sprite.getInterpolatedU(u / 4);
    }

    private float getInterpolatedV(float v, TextureAtlasSprite sprite) {
        return sprite.getInterpolatedV(v / 4);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particleTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType transform, MatrixStack stack) {
        TransformationMatrix matrix = getTransformationMatrix(transform);
        if (!matrix.isIdentity()) matrix.push(stack);
        return this;
    }

    private TransformationMatrix getTransformationMatrix(ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case GUI:
                return matrix(0, 0.05, 0, 30, 225, 0, 0.8, 0.8, 0.8);
            case GROUND:
                return matrix(0, 0, 0, 0, 3, 0, 0.25, 0.25, 0.25);
            case FIXED:
                return matrix(0, 0, 0, 0, 0, 0, 0.5, 0.5, 0.5);
            case THIRD_PERSON_LEFT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
                return matrix(0, 0, 0, 75, 45, 0, 0.5, 0.5, 0.5);
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
                return matrix(0, 0, 0, 0, 45, 0, 0.4, 0.4, 0.4);
        }

        return TransformationMatrix.identity();
    }

    private TransformationMatrix matrix(double tX, double tY, double tZ, double rX, double rY, double rZ, double sX, double sY, double sZ) {
        return new TransformationMatrix(new Vector3f((float) tX, (float) tY, (float) tZ),
                new Quaternion((float) rX, (float) rY, (float) rZ, true),
                new Vector3f((float) sX, (float) sY, (float) sZ),
                null);
    }
}
