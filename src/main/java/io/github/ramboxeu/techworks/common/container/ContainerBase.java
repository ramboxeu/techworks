package io.github.ramboxeu.techworks.common.container;

import net.minecraft.screen.ScreenHandler;

public abstract class ContainerBase extends ScreenHandler {
    public ContainerBase(int syncId) {
        super(null, syncId);
    }
}
