package io.github.ramboxeu.techworks.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.widget.*;
import io.github.ramboxeu.techworks.client.screen.widget.config.BaseConfigWidget;
import io.github.ramboxeu.techworks.client.screen.widget.config.IOConfigWidget;
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
    protected final RedstoneConfigWidget redstoneConfig;
    protected final WorkStatusConfigWidget workConfig;

    public BaseMachineScreen(CONTAINER container, PlayerInventory inv, ITextComponent title, ResourceLocation background, ResourceLocation machineFrontTex) {
        super(container, inv, title, background);
        addConfigWidget(new IOConfigWidget(this, machineFrontTex));
        addConfigWidget(container.getComponentsWidget().getScreenWidget(this));

        renderConfig = false;

        for (BaseContainerWidget widget : container.getWidgets()) {
            if (widget instanceof IPortScreenWidgetProvider<?>) {
                addPortWidget(((IPortScreenWidgetProvider<?>) widget).getPortScreenWidget(this));
            }
        }

        redstoneConfig = addWidget(new RedstoneConfigWidget(this, 176, 114, container.getMachineTile()));
        workConfig = addWidget(new WorkStatusConfigWidget(this, 176, 137, container.getMachineTile()));
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
            stack.translate(-(CONFIG_SCREEN_WIDTH / 2.0F), 0, 0);
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
            guiLeft -= CONFIG_SCREEN_WIDTH / 2;
            reInitWidgets();
        }
    }

    public void tabClicked(int tabIndex) {
        BaseConfigWidget widget = configWidgets.get(tabIndex);

        if (config == widget) {
            widget.toggle();
            config = null;
            guiLeft += (CONFIG_SCREEN_WIDTH / 2);
            renderConfig = false;
            configWidgets.forEach(BaseConfigWidget::updatePos);
            reInitWidgets();
        } else {
            if (!renderConfig) {
                guiLeft -= (CONFIG_SCREEN_WIDTH / 2);
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

        redstoneConfig.setActive(!renderConfig);
        workConfig.setActive(!renderConfig);
    }

    public boolean isConfigOpen() {
        return renderConfig;
    }
}
