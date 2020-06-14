package io.github.ramboxeu.techworks.common.api.component;

import net.minecraft.text.Text;

public abstract class ComponentType {
    private final Text name;
    private final Text description;

    public ComponentType(Text name, Text description) {
        this.name = name;
        this.description = description;
    }

    public Text getName() {
        return name;
    }

    public Text getDescription() {
        return description;
    }
}
