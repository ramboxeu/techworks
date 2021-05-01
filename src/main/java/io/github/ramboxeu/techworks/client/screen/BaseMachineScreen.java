package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.BaseContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.IPortScreenWidgetProvider;
import io.github.ramboxeu.techworks.client.screen.widget.PortScreenWidget;
import io.github.ramboxeu.techworks.client.screen.widget.config.BaseConfigWidget;
import io.github.ramboxeu.techworks.common.tile.BaseMachineTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMachineScreen<TILE extends BaseMachineTile, CONTAINER extends BaseMachineContainer<TILE>> extends BaseScreen<CONTAINER> {
    private static final int CONFIG_SCREEN_WIDTH = 130;

    private boolean renderConfig;
    private BaseConfigWidget config;

    protected final List<PortScreenWidget> portWidgets = new ArrayList<>();
    protected final List<BaseConfigWidget> configWidgets = new ArrayList<>();

    public BaseMachineScreen(CONTAINER screenContainer, PlayerInventory inv, ITextComponent titleIn, ResourceLocation background, ResourceLocation machineFrontTex) {
        super(screenContainer, inv, titleIn, background);

        renderConfig = false;

        for (BaseContainerWidget widget : screenContainer.getWidgets()) {
            if (widget instanceof IPortScreenWidgetProvider<?>) {
                addPortWidget(((IPortScreenWidgetProvider<?>) widget).getPortScreenWidget(this));
            }
        }
    }

    protected <T extends BaseConfigWidget> T addConfigWidget(T widget) {
        if (!configWidgets.contains(widget)) {
            configWidgets.add(widget);
            widget.initConfig(configWidgets.size() - 1, this);
        }

        addWidget(widget);

        return widget;
    }

    protected <T extends PortScreenWidget> T addPortWidget(T widget) {
        if (!portWidgets.contains(widget)) {
            portWidgets.add(widget);
        }

        addWidget(widget);

        return widget;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        if (renderConfig) {
            stack.push();
            stack.translate((CONFIG_SCREEN_WIDTH / 2.0F), 0, 0);
            super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);
            stack.pop();
        } else {
            super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);
        }
    }

    @Override
    protected void init() {
        super.init();

        if (renderConfig) {
            guiLeft += CONFIG_SCREEN_WIDTH / 2;
            reInitWidgets();
        }
    }

    public void tabClicked(int tabIndex) {
        Techworks.LOGGER.debug("Tab {} was clicked!", tabIndex);
        BaseConfigWidget widget = configWidgets.get(tabIndex);

        if (config == widget) {
            widget.toggle();
            config = null;
            guiLeft -= (CONFIG_SCREEN_WIDTH / 2);
            renderConfig = false;
            configWidgets.forEach(BaseConfigWidget::updatePos);
            reInitWidgets();
        } else {
            if (!renderConfig) {
                guiLeft += (CONFIG_SCREEN_WIDTH / 2);
                renderConfig = true;
                configWidgets.forEach(BaseConfigWidget::updatePos);
                reInitWidgets();
            }

            if (config != null) {
                config.toggle();
            }

            widget.toggle();
            config = widget;
        }
    }

    public boolean getRenderConfig() {
        return renderConfig;
    }
}