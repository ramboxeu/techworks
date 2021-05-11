package io.github.ramboxeu.techworks.client.screen.widget.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.BaseScreenWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

public abstract class BaseConfigWidget extends BaseScreenWidget {
    public static final int CONFIG_GUI_WIDTH = 130;
    public static final ResourceLocation GUI_LOCATION = new ResourceLocation(Techworks.MOD_ID, "textures/gui/config_gui.png");

    protected BaseMachineScreen<?, ?> machineScreen;
    protected boolean render;
    protected int tabX;
    protected int tabY;
    protected int index;

    protected final IReorderingProcessor tooltip;

    public BaseConfigWidget(BaseMachineScreen<?, ?> screen, IReorderingProcessor tooltip) {
        super(screen, 0, 0, 155, 166);
        this.tooltip = tooltip;

        render = false;
    }

    public void initConfig(int index, BaseMachineScreen<?, ?> screen) {
        this.index = index;

        machineScreen = screen;
        tabX = 0;
        tabY = (22 * index) + 7 + index;
    }

    @Override
    protected void renderWidget(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        minecraft.textureManager.bindTexture(GUI_LOCATION);

        if (machineScreen.isConfigOpen()) {
            stack.push();
            stack.translate(CONFIG_GUI_WIDTH, 0, 0);
            blit(stack, 0, tabY, 133, 0, 22, 22);
            stack.pop();
        } else {
            blit(stack, 0, tabY, 133, 0, 22, 22);
        }

        if (render) {
            blit(stack, -3, 0, 0, 0, 133, 166);
            renderConfig(stack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void renderToolTip(MatrixStack stack, int mouseX, int mouseY) {
        if (render) {
            super.renderToolTip(stack, mouseX, mouseY);
        }
    }

    @Override
    protected void renderWidgetTooltip(MatrixStack stack, int x, int y) {
        if (x >= tabX && y >= tabY && x <= tabX + 22 && y <= tabY + 22) {
            screen.renderTooltip(stack, Collections.singletonList(tooltip), x, y);
        }
    }

    @Override
    public void onScreenInit(Minecraft minecraft, int guiLeft, int guiTop, int guiWidth, int guiHeight) {
        super.onScreenInit(minecraft, guiLeft + guiWidth, guiTop, guiWidth, guiHeight);
    }

    // stack is translated
    protected abstract void renderConfig(MatrixStack stack, int mouseX, int mouseY, float partialTicks);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) (mouseX - guiLeft);
        int y = (int) (mouseY - guiTop);

        if (x >= tabX && y >= tabY && x <= tabX + 22 && y <= tabY + 22) {
            machineScreen.tabClicked(index);
            playDownSound(Minecraft.getInstance().getSoundHandler());
            return true;
        }

        return false;
    }

    public void toggle() {
        render = !render;
    }

    public void updatePos() {
        if (machineScreen.isConfigOpen()) {
            tabX += CONFIG_GUI_WIDTH;
        } else {
            tabX -= CONFIG_GUI_WIDTH;
        }
    }
}
