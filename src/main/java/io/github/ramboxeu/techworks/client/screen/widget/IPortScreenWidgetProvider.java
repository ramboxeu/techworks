package io.github.ramboxeu.techworks.client.screen.widget;

import io.github.ramboxeu.techworks.client.screen.BaseMachineScreen;

public interface IPortScreenWidgetProvider<T extends PortScreenWidget> {
    T getPortScreenWidget(BaseMachineScreen<?, ?> screen);
}
