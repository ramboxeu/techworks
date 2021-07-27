package io.github.ramboxeu.techworks.common.recipe;

import io.github.ramboxeu.techworks.common.registry.IItemSupplier;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class RecipeResult implements IRecipeResult {
    private final Item item;
    private final int count;

    public RecipeResult(IItemProvider item, int count) {
        this.item = item.asItem();
        this.count = count;
    }

    public RecipeResult(IItemSupplier item, int count) {
        this.item = item.getAsItem();
        this.count = count;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public int getCount() {
        return count;
    }
}
