package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.common.api.component.IComponent;
import io.github.ramboxeu.techworks.common.api.component.IComponentList;
import io.github.ramboxeu.techworks.common.api.component.IComponentProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ComponentItem<T extends IComponent> extends Item implements IComponentProvider {
    private final Factory<T> factory;

    public ComponentItem(Factory<T> factory) {
        super(new Settings().group(ItemGroup.REDSTONE).maxCount(1));
        this.factory = factory;
    }

    @Override
    public IComponent create(IComponentList list) {
        return this.factory.create(list);
    }

    public interface Factory<T extends IComponent> {
        T create(IComponentList list);
    }
}
