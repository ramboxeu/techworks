package io.github.ramboxeu.techworks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.common.tile.ItemTransporterTile;
import io.github.ramboxeu.techworks.common.util.cable.item.ItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class CableTileEntityRenderer extends TileEntityRenderer<ItemTransporterTile> {
    private static final int FULL_BRIGHT = ((15 << 20) | (15 << 4));

    private final ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

    public CableTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ItemTransporterTile tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        List<ItemPacket> packets = tile.getPackets();


        // current -> next


        for (ItemPacket packet : packets) {
//            Techworks.LOGGER.debug("Render");
            stack.push();
            Vector3d vec = packet.nextTransform(partialTicks);
//            Techworks.LOGGER.debug("Vec = {}", vec);
            stack.translate(vec.x, vec.y, vec.z); // 0.25 x 0.25 x 0.25
            // 0.1875 x 0.1875 x 0.1875
//            stack.scale(0.75f, 0.75f, 0.75f);
            stack.scale(0.5f, 0.5f, 0.5f);
//            stack.scale(4, 4, 4);
//            Techworks.LOGGER.debug("Rendering item");

//            Vector3d vec = makePosVector(tile.getPos(), item.getCurrentPos()) /*new Vector3d(1, 0, 0.5)*/;
//            Techworks.LOGGER.debug("Pos: {}", vec);

//            actualPos = lastTickPos + (pos - lastTickPos) * partialTicks
//            stack.translate(0, 0, 0.375);
            renderer.renderItem(packet.getStack(), ItemCameraTransforms.TransformType.GROUND, FULL_BRIGHT, combinedOverlay, stack, buffer);
            stack.pop();
        }

//        stack.push();
//        renderer.renderItem(STACK, ItemCameraTransforms.TransformType.GROUND, FULL_BRIGHT, combinedOverlay, stack, buffer);
//        stack.pop();
    }

//    private static void transform(MatrixStack stack, Direction dir, Vector3d vec) {
//        boolean isPositive = DirectionUtils.isPositive(dir);
//
//        switch (dir.getAxis()) {
//            case X:
//                stack.translate((vec.x / 2) * (isPositive ? 1 : -1), vec.y / 2, vec.z / 2);
//                break;
//            case Y:
//                stack.translate(vec.x / 2, (vec.y / 2) * (isPositive ? 1 : -1), vec.z / 2);
//                break;
//            case Z:
//                stack.translate(vec.x / 2, vec.y / 2, (vec.z / 2) * (isPositive ? 1 : -1));
//                break;
//        }
//    }
//
//    private static Vector3d makePosVector(BlockPos origin, BlockPos current) {
//        return new Vector3d(Math.abs(origin.getX() - current.getX()), Math.abs(origin.getY() - current.getY()), Math.abs(origin.getZ() - current.getZ()));
//    }
}
