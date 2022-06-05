package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.common.tile.AnvilIngotHolderTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;

public class AnvilIngotHolderTileRenderer extends TileEntityRenderer<AnvilIngotHolderTile> {
    private final ItemRenderer renderer;

    public AnvilIngotHolderTileRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        renderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(AnvilIngotHolderTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buf, int combinedLight, int combinedOverlay) {
        if (tile.getStack() != null) {
            stack.push();
//            stack.translate(0, 0, 0);
            stack.translate(.5, 0, .5);
            stack.scale(.5f, tile.getStack().getCount(), .5f);
            stack.rotate(Vector3f.YN.rotationDegrees(90));
            stack.rotate(Vector3f.XN.rotationDegrees(90));
//            stack.rotate(new Quaternion(new Vector3f(-1, -1, 0), (float) (2.0 / Math.PI), false));
//            stack.translate(0, 0, 0);
//            stack.scale(.5f, 1, .5f);
            renderer.renderItem(tile.getStack(), ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, stack, buf);
            stack.pop();
        }
    }
}
