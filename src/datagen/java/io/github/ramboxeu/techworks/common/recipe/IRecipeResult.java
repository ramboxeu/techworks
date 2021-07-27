package io.github.ramboxeu.techworks.common.recipe;

import net.minecraft.item.Item;

@FunctionalInterface
public interface IRecipeResult {
    Item getItem();

    default int getCount() {
        return 1;
    }
}
