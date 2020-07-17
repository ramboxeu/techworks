package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BurnProgressWidget extends BaseWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/burn_progress.png");

    private float progress;

    public BurnProgressWidget(int x, int y) {
        super(x, y, 14, 14);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            Minecraft.getInstance().textureManager.bindTexture(TEXTURE);
            blit(stack, x, y, 0, 0, 14, 14, 28, 14);
            if (progress > 0) {
                blit(stack, x, y + (int)(14 - (14 * progress)), 14, (int)(14 - (14 * progress)), 14, (int)(14 - (14 * progress)), 28, 14);
            }
        }
    }

    public void setProgress(float progress) {
        this.progress = Utils.clampFloat(progress);
    }
}
