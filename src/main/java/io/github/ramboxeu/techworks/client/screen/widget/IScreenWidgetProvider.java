package io.github.ramboxeu.techworks.client.screen.widget;

import io.github.ramboxeu.techworks.client.screen.BaseScreen;

import javax.annotation.Nonnull;

public interface IScreenWidgetProvider<T extends BaseScreenWidget> {
    @Nonnull
    T getScreenWidget(BaseScreen<?> screen);
}
