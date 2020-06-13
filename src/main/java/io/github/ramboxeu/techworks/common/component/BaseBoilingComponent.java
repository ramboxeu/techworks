package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;

public abstract class BaseBoilingComponent implements IComponent {
    private final IComponentList<?> componentList;
    private final IComponentProvider provider;
    private int counter;

    public BaseBoilingComponent(IComponentList<?> componentList, IComponentProvider provider) {
        this.componentList = componentList;
        this.provider = provider;
    }

    @Override
    public ComponentType getType() {
        return new BoilingComponentType();
    }

    @Override
    public IComponentProvider getProvider() {
        return provider;
    }

    @Override
    public void tick() {
        counter++;
        if (counter >= 20) {
            Techworks.LOG.info("One second has passed!");
        }
    }
}
