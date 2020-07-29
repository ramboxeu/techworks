package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BurnProgressWidget extends BaseWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/burn_progress.png");

    private float progress;
    private boolean reverse;

    // reverse - if set to true the bar will decrease
    public BurnProgressWidget(int x, int y, boolean reverse) {
        super(x, y, 14, 14);

        this.reverse = reverse;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().textureManager.bindTexture(TEXTURE);
        blit(stack, x, y, 0, 0, 14, 14, 28, 14);

        if (progress > 0) {
            // Casting to int loses some precision, while vertexes are perfectly fine with floats
            // Maybe create custom blit function with float support
            int texHeight = (int)(14 * (reverse ? progress : (1 - progress)));
            blit(stack, x, y + texHeight, 14, texHeight, 14, (14 - texHeight), 28, 14);
        }
    }

    public void setProgress(float progress) {
        this.progress = Utils.clampFloat(progress);
    }
}
