package io.github.ramboxeu.techworks.client.screen.widget.inventory;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

@FunctionalInterface
public interface SlotFactory {
    SlotItemHandler create(IItemHandler handler, int index, int x, int y);
}
