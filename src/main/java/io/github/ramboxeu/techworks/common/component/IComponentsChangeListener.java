package io.github.ramboxeu.techworks.common.component;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IComponentsChangeListener<T extends Component> {
    void onComponentsChanged(T component, ItemStack stack);
}
