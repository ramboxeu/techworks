package io.github.ramboxeu.techworks.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.screen.BaseScreen;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import io.github.ramboxeu.techworks.common.util.StandbyMode;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;

public class WorkStatusConfigWidget extends BaseScreenWidget {
    private static final ResourceLocation TEX = new ResourceLocation(Techworks.MOD_ID, "textures/gui/widget/controls.png");

    private final BaseMachineTile tile;
    private StandbyMode mode;

    public WorkStatusConfigWidget(BaseScreen<?> screen, int x, int y, BaseMachineTile tile) {
        super(screen, x, y, 22, 22);
        this.tile = tile;
        mode = tile.getWorkMode();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) (mouseX - guiLeft);
        int y = (int) (mouseY - guiTop);

        if (x >= this.x + 3 && y >= this.y + 2 && x <= this.x + 19 && y <= this.y + 18) {
            mode = mode.next();
            tile.syncStandbyMode(mode);
            return true;
        }

        return false;
    }

    @Override
    protected void renderBaseWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (active) {
            minecraft.textureManager.bindTexture(TEX);
            blit(stack, x, y, 22, 22, 0, 0, 22, 22, 64, 30);

            int offset = getTexOffset();
            blit(stack, x + 2, y + 3, 16, 16, offset, 14, 16, 16, 64, 30);
        }
    }

    private int getTexOffset() {
        switch (mode) {
            case OFF:
                return 22;
            case ON:
                return 38;
        }

        throw new AssertionError();
    }

    @Override
    protected void renderWidgetTooltip(MatrixStack stack, int mouseX, int mouseY) {
        if (active) {
            if (mouseX > x && mouseY >= y && mouseX <= x + width && mouseY <= y + height) {
                renderTooltip(stack, TranslationKeys.STATUS.text().appendString(": ").appendSibling(getModeName()), mouseX, mouseY);
            }
        }
    }

    private IFormattableTextComponent getModeName() {
        switch (mode) {
            case OFF:
                return TranslationKeys.ACTIVE.text();
            case ON:
                return TranslationKeys.STANDBY.text();
        }

        throw new AssertionError();
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
