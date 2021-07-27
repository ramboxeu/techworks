package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.item.Item;

@FunctionalInterface
public interface IItemSupplier {
    Item getAsItem();
}
