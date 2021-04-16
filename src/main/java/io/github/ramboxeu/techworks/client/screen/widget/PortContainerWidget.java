package io.github.ramboxeu.techworks.client.screen.widget;

import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;

public abstract class PortContainerWidget extends BaseContainerWidget {
    protected final HandlerData data;

    public PortContainerWidget(BaseMachineContainer<?> container, HandlerData data) {
        container.addData(data);

        this.data = data;
    }
}
