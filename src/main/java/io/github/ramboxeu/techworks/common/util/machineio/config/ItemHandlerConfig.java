package io.github.ramboxeu.techworks.common.util.machineio.config;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.machineio.AutoMode;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerConfig extends HandlerConfig {
    private final ItemHandlerData data;
    private final IItemHandler handler;
    private final int minSlot;
    private final int slots;
    private int firstSlot;
    private int lastSlot;

    public ItemHandlerConfig(ItemHandlerData data) {
        this(StorageMode.BOTH, data);
    }

    public ItemHandlerConfig(StorageMode mode, ItemHandlerData data) {
        this(mode, AutoMode.OFF, data);
    }

    public ItemHandlerConfig(StorageMode mode, AutoMode autoMode, ItemHandlerData data) {
        super(mode, autoMode, data);
        this.data = data;

        int maxSlot = data.getMaxSlot();
        handler = data.getHandler();
        minSlot = data.getMinSlot();
        slots = (maxSlot - minSlot) + 1;
    }

    public void setSlots(int firstSlot, int lastSlot) {
        Techworks.LOGGER.debug("Set to slot {} {}", firstSlot, lastSlot);
        this.firstSlot = firstSlot;
        this.lastSlot = lastSlot;
    }

    public int getHandlerSlot(int slot) {
        return minSlot + (slot - firstSlot);
    }

    public boolean matchesSlot(int slot) {
        return slot >= firstSlot && slot <= lastSlot;
    }

    public int getSlots() {
        return slots;
    }

    public void adjustSlots(int adjustment) {
        firstSlot -= adjustment;
        lastSlot -= adjustment;
    }

    public ItemHandlerData getData() {
        return data;
    }

    public IItemHandler getHandler() {
        return handler;
    }

    public int getLastSlot() {
        return lastSlot;
    }
}
