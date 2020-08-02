package io.github.ramboxeu.techworks.client.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.lwjgl.opengl.GL11;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

public class TechworksRenderTypes {
    private static final VertexFormat POSITION_TEX_LIGHTMAP = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_3F).add(TEX_2F).add(TEX_2SB).build());

    // For now machine io sprites will stitched onto block atlas
    private static final RenderState.TextureState BLOCK_SHEET = new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false);
    private static final RenderState.LightmapState LIGHTMAP_ENABLED = new RenderState.LightmapState(true);
    private static final RenderState.AlphaState ALPHA_DEFAULT = new RenderState.AlphaState(0.003921569f);

    // Buffer size was taken from solid render type
    public static final RenderType MACHINE = RenderType.makeType("machine", POSITION_TEX_LIGHTMAP, GL11.GL_QUADS, 2097152,
            RenderType.State.getBuilder().
                    texture(BLOCK_SHEET).
                    lightmap(LIGHTMAP_ENABLED).
                    alpha(ALPHA_DEFAULT).
                    build(false)
    );
}
