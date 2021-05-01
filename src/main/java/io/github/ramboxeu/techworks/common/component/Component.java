package io.github.ramboxeu.techworks.common.component;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public abstract class Component implements IItemProvider {

    private final ComponentType<?> type;
    protected final ResourceLocation id;
    protected final Item item;

    public Component(ComponentType<?> type, ResourceLocation id, Item item) {
        this.type = type;
        this.id = id;
        this.item = item;
    }

    @Override
    public Item asItem() {
        return item;
    }

    public ComponentType<?> getType() {
        return type;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public boolean isBase() {
        return this.id.equals(type.getBaseComponentId());
    }
}
