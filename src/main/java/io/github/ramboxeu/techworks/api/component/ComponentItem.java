package io.github.ramboxeu.techworks.api.component;

import net.minecraft.item.Item;

public abstract class ComponentItem extends Item {

    public ComponentItem(Properties properties) {
        super(properties);
    }

    public abstract int getLevel();
}
