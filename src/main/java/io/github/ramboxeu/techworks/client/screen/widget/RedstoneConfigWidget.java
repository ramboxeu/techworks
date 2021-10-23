package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.RedstoneMode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

public class RedstoneConfigWidget extends BaseScreenWidget {
    private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/controls.png");

    private final BaseMachineTile tile;
    private RedstoneMode mode;

    public RedstoneConfigWidget(BaseScreen<?> screen, int x, int y, BaseMachineTile tile) {
        super(screen, x, y, 22, 22);
        this.tile = tile;
        mode = tile.getRedstoneMode();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (active) {
            int x = (int) (mouseX - guiLeft);
            int y = (int) (mouseY - guiTop);

            if (x >= this.x + 3 && y >= this.y + 4 && x <= this.x + 17 && y <= this.y + 18) {
                mode = button == 0 ? mode.next() : mode.previous();
                tile.syncRedstoneMode(mode);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (active) {
            minecraft.textureManager.bindTexture(TEX);
            blit(stack, x, y, 22, 22, 0, 0, 22, 22, 64, 30);

            int offset = getTexOffset();
            blit(stack, x + 3, y + 4, 14, 14, offset, 0, 14, 14, 64, 30);
        }
    }

    private int getTexOffset() {
        switch (mode) {
            case IGNORE:
                return 22;
            case LOW:
                return 36;
            case HIGH:
                return 50;
        }

        throw new AssertionError();
    }

    @Override
    protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {
        if (active) {
            if (mouseX > x && mouseY >= y && mouseX <= x + width && mouseY <= y + height) {
                renderTooltip(stack, TranslationKeys.REDSTONE.text().appendString(": ").appendSibling(getModeName()), mouseX, mouseY);
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private IFormattableTextComponent getModeName() {
        switch (mode) {
            case IGNORE:
                return TranslationKeys.IGNORED.text();
            case HIGH:
                return TranslationKeys.HIGH.text();
            case LOW:
                return TranslationKeys.LOW.text();
        }

        throw new AssertionError();
    }
}
