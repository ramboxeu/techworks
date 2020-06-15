package io.github.ramboxeu.techworks.client.screen;

import io.github.ramboxeu.techworks.common.api.widget.Widget;
import io.github.ramboxeu.techworks.common.container.machine.BoilerContainer;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class BoilerScreen extends ContainerScreen<BoilerContainer> {
    private List<Widget> widgetList;

    public BoilerScreen(BoilerContainer container) {
        super(container, container.getPlayerInventory(), new TranslatableText("screen.techworks.boiler"));

        widgetList = container.getWidgets();
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {

    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        //font.draw(String.valueOf(container.getDummyInt()), 10, 10, 4210752);
        for (Widget widget : widgetList) {
            widget.render(mouseX, mouseY, this, minecraft, container);
        }
    }

    @Override
    protected void drawMouseoverTooltip(int mouseX, int mouseY) {
        for (Widget widget : widgetList) {
            widget.buildTooltip(mouseX, mouseY);
        }
    }
}
