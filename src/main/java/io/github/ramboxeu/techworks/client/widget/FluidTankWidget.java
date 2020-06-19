package io.github.ramboxeu.techworks.client.widget;

import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import io.github.ramboxeu.techworks.common.util.FluidStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.Container;

public class FluidTankWidget extends Widget {
    private final int dataId;

    public FluidTankWidget(int x, int y, int width, int height, int dataId) {
        super(x, y, width, height);

        this.dataId = dataId;
    }

    @Override
    public void render(int mouseX, int mouseY, Screen screen, MinecraftClient client, Container container) {
        super.render(mouseX, mouseY, screen, client, container);

        ((AbstractMachineContainer<?>)container).getData(dataId).ifPresent(o -> {
            FluidStack stack = (FluidStack) o;
            client.textRenderer.draw(String.format("%dmb of %s", stack.getAmount(), stack.getFluid()), x, y, 4210752);
        });
    }
}
