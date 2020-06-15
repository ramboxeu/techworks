package io.github.ramboxeu.techworks.common.api.widget;

import io.github.ramboxeu.techworks.common.container.AbstractMachineContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.container.Container;

import java.util.ArrayList;
import java.util.List;

public class GasTankWidget extends Widget {
    private int dataIndex;

    public GasTankWidget(int x, int y, int width, int height, int dataIndex) {
        super(x, y, width, height);

        this.dataIndex = dataIndex;
    }

    @Override
    public void render(int mouseX, int mouseY, Screen screen, MinecraftClient client, Container container) {
        client.textRenderer.draw(String.format("(%d, %d)", mouseX, mouseY), 0,0, 4210752);
        int data = ((AbstractMachineContainer<?>) container).getSyncedValueOrDefault(dataIndex, -1);
        client.textRenderer.draw(String.format("%d", data), 0,10, 4210752);
    }

    @Override
    public List<String> buildTooltip(int mouseX, int mouseY) {
        return new ArrayList<String>(){
            {
                add("test");
                add("line");
                add("number 1");
            }
        };
    }
}
