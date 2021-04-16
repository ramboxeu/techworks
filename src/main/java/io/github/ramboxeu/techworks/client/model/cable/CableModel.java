package io.github.ramboxeu.techworks.client.model.cable;

import com.mojang.datafixers.util.Pair;
import io.github.ramboxeu.techworks.client.util.TextureCoords;
import io.github.ramboxeu.techworks.client.util.TextureCoordsGroup;
import io.github.ramboxeu.techworks.common.util.Cuboid;
import io.github.ramboxeu.techworks.common.util.CuboidGroup;
import io.github.ramboxeu.techworks.common.util.DirectionUtils;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.*;
import java.util.function.Function;

public class CableModel implements IModelGeometry<CableModel> {
    public static final CuboidGroup DOWN = CuboidGroup.of(Cuboid.make(5.5, 2, 5.5, 10.5, 5.5, 10.5),
            Cuboid.make(4.25, 0, 4.25, 11.75, 2, 11.75),
            Cuboid.make(5.5, 0, 5.5, 10.5, 5.5, 10.5));

    public static final CuboidGroup UP = CuboidGroup.of(Cuboid.make(5.5, 10.5, 5.5, 10.5, 14, 10.5),
            Cuboid.make(4.25, 14, 4.25, 11.75, 16, 11.75),
            Cuboid.make(5.5, 10.5, 5.5, 10.5, 16, 10.5));

    public static final CuboidGroup NORTH = CuboidGroup.of(Cuboid.make(5.5, 5.5, 2, 10.5, 10.5, 5.5),
            Cuboid.make(4.25, 4.25, 0, 11.75, 11.75, 2),
            Cuboid.make(5.5, 5.5, 0, 10.5, 10.5, 5.5));

    public static final CuboidGroup SOUTH = CuboidGroup.of(Cuboid.make(5.5, 5.5, 10.5, 10.5, 10.5, 14),
            Cuboid.make(4.25, 4.25, 14, 11.75, 11.75, 16),
            Cuboid.make(5.5, 5.5, 10.5, 10.5, 10.5, 16));

    public static final CuboidGroup WEST = CuboidGroup.of(Cuboid.make(2, 5.5, 5.5, 5.5, 10.5, 10.5),
            Cuboid.make(0, 4.25, 4.25, 2, 11.75, 11.75),
            Cuboid.make(0, 5.5, 5.5, 5.5, 10.5, 10.5));

    public static final CuboidGroup EAST = CuboidGroup.of(Cuboid.make(10.5, 5.5, 5.5, 14, 10.5, 10.5),
            Cuboid.make(14, 4.25, 4.25, 16, 11.75, 11.75),
            Cuboid.make(10.5, 5.5, 5.5, 16, 10.5, 10.5));

    public static final Cuboid CENTER = Cuboid.make(5.5, 5.5, 5.5, 10.5, 10.5, 10.5);

    public static final TextureCoordsGroup HORIZONTAL = TextureCoordsGroup.of(TextureCoords.make(0, 0, 30, 30),
            TextureCoords.make(30, 0, 38, 30),
            TextureCoords.make(0, 30, 30, 38),
            TextureCoords.make(46, 0, 54, 30),
            TextureCoords.make(0, 46, 30, 54),
            TextureCoords.make(38, 0, 46, 30),
            TextureCoords.make(0, 38, 30, 46),
            TextureCoords.make(0, 20, 14, 40),
            TextureCoords.make(15, 20, 35, 34),
            TextureCoords.make(20, 40, 42, 60),
            TextureCoords.make(0, 40, 20, 62),
            TextureCoords.make(0, 0, 20, 20),
            TextureCoords.make(20, 0, 40, 20));

    public static final TextureCoordsGroup VERTICAL = TextureCoordsGroup.of(TextureCoords.make(0, 0, 30, 30),
            TextureCoords.make(0, 30, 30, 38),
            TextureCoords.make(0, 46, 30, 54),
            TextureCoords.make(0, 38, 30, 46),
            TextureCoords.make(15, 20, 35, 34),
            TextureCoords.make(0, 40, 20, 62),
            TextureCoords.make(0, 0, 20, 20),
            TextureCoords.make(20, 0, 40, 20));

    public static final Map<Direction, CuboidGroup> CUBOID_MAP = new EnumMap<>(Direction.class);
    static {
        CUBOID_MAP.put(Direction.DOWN, DOWN);
        CUBOID_MAP.put(Direction.UP, UP);
        CUBOID_MAP.put(Direction.NORTH, NORTH);
        CUBOID_MAP.put(Direction.SOUTH, SOUTH);
        CUBOID_MAP.put(Direction.EAST, EAST);
        CUBOID_MAP.put(Direction.WEST, WEST);
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new CableBakedModel(resolveTexture(owner, spriteGetter, "base"),
                resolveTexture(owner, spriteGetter, "particle"),
                resolveTexture(owner, spriteGetter, "connector"));
    }

    private static TextureAtlasSprite resolveTexture(IModelConfiguration owner, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, String name) {
        if (owner.isTexturePresent(name))
            return spriteGetter.apply(owner.resolveTexture(name));

        return spriteGetter.apply(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, MissingTextureSprite.getLocation()));
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<RenderMaterial> textures = new HashSet<>();

        if (owner.isTexturePresent("base")) textures.add(owner.resolveTexture("base"));
        if (owner.isTexturePresent("particle")) textures.add(owner.resolveTexture("particle"));
        if (owner.isTexturePresent("connector")) textures.add(owner.resolveTexture("connector"));

        return textures;
    }

    public static CuboidGroup getCuboidGroup(Direction direction) {
        return CUBOID_MAP.get(direction);
    }

    public static TextureCoordsGroup getTextureCoords(Direction direction) {
        return DirectionUtils.isHorizontal(direction) ? HORIZONTAL : VERTICAL;
    }
}
