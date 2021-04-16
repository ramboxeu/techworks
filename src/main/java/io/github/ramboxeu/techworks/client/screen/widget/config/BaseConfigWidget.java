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
        super(screen, -22, 0, 155, 166);
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

        stack.push();
        stack.translate(-22, 0, 0);
        blit(stack, 0, tabY, 133, 0, 22, 22);
        stack.pop();

        if (render) {
            blit(stack, 0, 0, 0, 0, 133, 166);
            renderConfig(stack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void renderToolTip(MatrixStack stack, int mouseX, int mouseY) {
        int x = (mouseX - guiLeft);
        int y = (mouseY - guiTop);
//        Techworks.LOGGER.debug("x = {} y = {} tX = {} tY = {}", x, y, tabX, tabY);

//        if (machineScreen.getRenderConfig()) {
//            x += CONFIG_GUI_WIDTH;
//        }

        if (x + 22 >= tabX && y >= tabY && x + 22 <= tabX + 22 && y <= tabY + 22) {
            screen.renderTooltip(stack, Collections.singletonList(tooltip), x, y);
        }
    }

    @Override
    public void onScreenInit(Minecraft minecraft, int guiLeft, int guiTop, int guiWidth, int guiHeight) {
        if (machineScreen.getRenderConfig()) {
            guiLeft -= CONFIG_GUI_WIDTH;
        }

        super.onScreenInit(minecraft, guiLeft, guiTop, guiWidth, guiHeight);
    }

    // stack is translated
    protected abstract void renderConfig(MatrixStack stack, int mouseX, int mouseY, float partialTicks);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) (mouseX - guiLeft);
        int y = (int) (mouseY - guiTop);

//        if (machineScreen.getRenderConfig()) {
//            x += CONFIG_GUI_WIDTH;
//        }

//        Techworks.LOGGER.debug("x = {} y = {} tX = {} tY = {}", x, y, tabX, tabY);
        if (x + 22 >= tabX && y >= tabY && x + 22 <= tabX + 22 && y <= tabY + 22) {
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
    }
}
