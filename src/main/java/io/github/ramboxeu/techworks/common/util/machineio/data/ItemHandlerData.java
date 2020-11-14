package io.github.ramboxeu.techworks.common.util.machineio.data;

import io.github.ramboxeu.techworks.common.util.Side;
import io.github.ramboxeu.techworks.common.util.machineio.InputType;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerData extends HandlerData {
    private final IItemHandler handler;
    private final int minSlot;
    private final int maxSlot;

    public ItemHandlerData(Side side, InputType type, int color, int identity, IItemHandler handler, int minSlot, int maxSlot) {
        super(type, color, identity, handler);
        this.handler = handler;
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    public IItemHandler getHandler() {
        return handler;
    }

    public int getMinSlot() {
        return minSlot;
    }

    public int getMaxSlot() {
        return maxSlot;
    }
}
