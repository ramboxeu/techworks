package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import io.github.ramboxeu.techworks.common.registry.ComponentTypes;

public abstract class BaseBoilingComponent implements IComponent {
    private final IComponentList<?> componentList;
    private final IComponentProvider provider;
    private int counter;

    public BaseBoilingComponent(IComponentList<?> componentList, IComponentProvider provider) {
        Techworks.LOG.info("List: {}, Provider: {}", componentList, provider);
//        Techworks.LOG.info("Provider identifier form registry: {}", TechworksRegistries.COMPONENT_PROVIDER.getId(provider));
        this.componentList = componentList;
        this.provider = provider;
    }

    @Override
    public ComponentType getType() {
        return ComponentTypes.BOILING_COMPONENT;
    }

    @Override
    public IComponentProvider getProvider() {
        return provider;
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
