package io.github.ramboxeu.techworks.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;

public abstract class BaseConfigWidget extends AbstractGui implements IGuiEventListener, IRenderable {
    protected final BaseMachineScreen<?, ?> screen;
    protected final Minecraft minecraft;

    public BaseConfigWidget(BaseMachineScreen<?, ?> screen) {
        this.screen = screen;
        minecraft = Minecraft.getInstance();
    }

    public boolean isSame(Class<? extends BaseConfigWidget> clazz) {
        return this.getClass() == clazz;
    }
}
