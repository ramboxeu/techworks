package io.github.ramboxeu.techworks.client.screen.widget.inventory;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;
import io.github.ramboxeu.techworks.client.screen.widget.IPortScreenWidgetProvider;
import io.github.ramboxeu.techworks.client.screen.widget.PortContainerWidget;
import io.github.ramboxeu.techworks.client.screen.widget.PortScreenWidget;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotWidget extends PortContainerWidget implements IPortScreenWidgetProvider<SlotWidget.ScreenWidget> {
    private int x;
    private int y;
    private int index;
    private boolean large;
    private final IItemHandler handler;
    private final SlotFactory factory;

    private ScreenWidget screenWidgetInstance;

    public SlotWidget(BaseMachineContainer<?> container, int x, int y, int index, boolean large, ItemHandlerData data) {
        this(container, x, y, index, large, data, SlotItemHandler::new);
    }

    public SlotWidget(BaseMachineContainer<?> container, int x, int y, int index, boolean large, ItemHandlerData data, SlotFactory factory) {
        super(container, data);
        this.x = x;
        this.y = y;
        this.factory = factory;
        this.index = index;
        this.large = large;

        handler = data.getHandler();
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        builder.slot(factory.create(handler, index, x + (large ? 5 : 1), y + (large ? 5 : 1)));
    }

    @Override
    public ScreenWidget getPortScreenWidget(BaseMachineScreen<?, ?> screen) {
        if (screenWidgetInstance == null) {
            screenWidgetInstance = new ScreenWidget(screen, x, y, large, data);
        }

        return screenWidgetInstance;
    }

    public static class ScreenWidget extends PortScreenWidget {

        public ScreenWidget(BaseMachineScreen<?, ?> screen, int x, int y, boolean large, HandlerData data) {
            super(screen, x, y, (large ? 26 : 18), (large ? 26 : 18), data);
        }
    }
}
