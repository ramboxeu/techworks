package io.github.ramboxeu.techworks.common.component;

import io.github.ramboxeu.techworks.common.api.component.ComponentType;
import net.minecraft.text.LiteralText;

public class BoilingComponentType extends ComponentType {
    public BoilingComponentType() {
        super(new LiteralText("Boiling"), new LiteralText("Boils water"));
    }
}
