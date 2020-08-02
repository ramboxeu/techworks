package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.ramboxeu.techworks.client.util.TechworksRenderTypes;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.tile.machine.MachineIO;
import io.github.ramboxeu.techworks.common.tile.machine.MachinePort;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class MachineTileEntityRenderer extends TileEntityRenderer<BaseMachineTile> {
    private static final Minecraft MC = Minecraft.getInstance();

    public MachineTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BaseMachineTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        IVertexBuilder builder = buffer.getBuffer(TechworksRenderTypes.MACHINE);
        MC.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        MachineIO machineIO = tile.getMachineIO();

        for (Direction facing : Direction.values()) {
            MachinePort port = machineIO.getPort(facing);
            if (port.isDisabled()) continue;

            renderPort(facing, port, stack, builder);
        }

        ((IRenderTypeBuffer.Impl) buffer).finish();
    }

    private void renderPort(Direction facing, MachinePort port, MatrixStack stack, IVertexBuilder builder) {
        ResourceLocation loc = port.getType().getSpriteLocation(port.getMode());
        if (loc == null) return;

        TextureAtlasSprite sprite = MC.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(loc);

        stack.push();
        stack.translate(.5f, .5f, .5f);

        switch (facing) {
            case UP:
                stack.rotate(Vector3f.XP.rotationDegrees(90));
                break;
            case DOWN:
                stack.rotate(Vector3f.XP.rotationDegrees(-90));
                break;
            case SOUTH:
                stack.rotate(Vector3f.YP.rotationDegrees(180));
                break;
            case WEST:
                stack.rotate(Vector3f.YP.rotationDegrees(90));
                break;
            case EAST:
                stack.rotate(Vector3f.YP.rotationDegrees(-90));
                break;
            default:
                break;
        }

        stack.translate(-.5, -.5f, -.5f);

        Matrix4f matrix = stack.getLast().getMatrix();
        // The lightning is a bit off
        builder.pos(matrix, .25f, .75f, -0.0001f).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(0, 240).endVertex();
        builder.pos(matrix, .75f, .75f, -0.0001f).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(0, 240).endVertex();
        builder.pos(matrix, .75f, .25f, -0.0001f).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(0, 240).endVertex();
        builder.pos(matrix, .25f, .25f, -0.0001f).tex(sprite.getMinU(), sprite.getMinV()).lightmap(0, 240).endVertex();

        stack.pop();
    }
}
