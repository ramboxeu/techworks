package io.github.ramboxeu.techworks.common.component;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public abstract class Component {

    private final ComponentType<?> type;
    protected final ResourceLocation id;
    protected final Item item;

    public Component(ComponentType<?> type, ResourceLocation id, Item item) {
        this.type = type;
        this.id = id;
        this.item = item;
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
}
