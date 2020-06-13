package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;

public class BasicBoilingComponent extends BaseBoilingComponent {

    public BasicBoilingComponent(IComponentList<?> componentList, IComponentProvider provider) {
        super(componentList, provider);
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
