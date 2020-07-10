package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.api.component.ComponentStackHandler;
import net.minecraft.util.text.ITextComponent;

public interface IComponentsContainerProvider {
    ComponentStackHandler getComponentsStackHandler();

    ITextComponent getComponentsDisplayName();
}
