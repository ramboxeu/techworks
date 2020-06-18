package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.sync.EventEmitter;
import io.github.ramboxeu.techworks.common.registry.ComponentTypes;

public abstract class BaseBoilingComponent implements IComponent {
    private final IComponentList<?> componentList;
    private int counter;

    public BaseBoilingComponent(IComponentList<?> componentList) {
        this.componentList = componentList;
    }

    @Override
    public ComponentType getType() {
        return ComponentTypes.BOILING_COMPONENT;
    }

    @Override
    public void tick() {
        counter++;
        if (counter >= 80) {
            Techworks.LOG.info("4 seconds has passed!");
            counter = 0;
        }
    }

//    @Override
//    public CompoundTag toTag(CompoundTag tag) {
//        Techworks.LOG.info("Writing to: {}", tag);
//    }
//
//    @Override
//    public void fromTag(CompoundTag tag) {
//        Techworks.LOG.info("Reading from: {}", tag);
//    }
}
