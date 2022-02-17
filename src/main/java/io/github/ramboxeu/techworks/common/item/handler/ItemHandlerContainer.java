package io.github.ramboxeu.techworks.common.item.handler;

import io.github.ramboxeu.techworks.common.util.Utils;
import io.github.ramboxeu.techworks.common.util.machineio.AutoMode;
import io.github.ramboxeu.techworks.common.util.machineio.StorageMode;
import io.github.ramboxeu.techworks.common.util.machineio.config.HandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.config.ItemHandlerConfig;
import io.github.ramboxeu.techworks.common.util.machineio.data.HandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import io.github.ramboxeu.techworks.common.util.machineio.handler.IHandlerContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
 * This handler is not saved, nor it saves its children. This means handlers should be saved separately.
 */
public class ItemHandlerContainer implements IItemHandler, IHandlerContainer {
    private final List<ItemHandlerConfig> ranges;

    public ItemHandlerContainer() {
        ranges = new ArrayList<>();
    }

    public void addHandlers(ItemHandlerConfig... itemsData) {
        addHandlers(Arrays.stream(itemsData).collect(Collectors.toList()));
    }

    public void addHandlers(List<ItemHandlerConfig> configs) {
        for (ItemHandlerConfig config : configs) {
            addHandler(config);
        }
    }

    public ItemHandlerConfig addHandler(ItemHandlerConfig config) {
        ItemHandlerConfig existing = Utils.getExistingConfig(ranges, config.getData());

        if (existing != null) {
            return existing;
        }

        int endSlot = ranges.stream()
                .max(Comparator.comparingInt(ItemHandlerConfig::getLastSlot))
                .map(ItemHandlerConfig::getLastSlot)
                .orElse(-1);

        ItemHandlerData data = config.getData();
        int firstSlot = endSlot + 1;
        int lastSlot = data.getMaxSlot() - data.getMinSlot() + endSlot + 1;

        config.setSlots(firstSlot, lastSlot);
        ranges.add(config);
        return config;
    }

    @Override
    public HandlerConfig remove(HandlerData data) {
        return ranges.stream().filter(config -> config.getBaseData() == data).findFirst().map(config -> {
            ItemHandlerConfig removed = ranges.remove(ranges.indexOf(config));
            int adjustment = removed.getSlots();

            ranges.forEach(range -> range.adjustSlots(adjustment));
            return removed;
        }).orElse(null);
    }

    @Override
    public void setStorageMode(HandlerData data, StorageMode mode, AutoMode autoMode) {
        ranges.stream().filter(config -> config.getBaseData() == data).findFirst().ifPresent(config -> {
            config.setMode(mode);
            config.setAutoMode(autoMode);
        });
    }

    @Override
    public List<HandlerConfig> getConfigs() {
        return Collections.unmodifiableList(ranges);
    }

    private Optional<ItemHandlerConfig> getRange(int slot, Predicate<StorageMode> modePredicate) {
        return ranges.stream().filter(range -> range.matchesSlot(slot) && modePredicate.test(range.getMode())).findFirst();
    }

    @Override
    public int getSlots() {
        return ranges.stream().mapToInt(ItemHandlerConfig::getSlots).sum();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        Optional<ItemHandlerConfig> range = getRange(slot, mode -> true);
        return range.map(value -> value.getHandler().getStackInSlot(value.getHandlerSlot(slot))).orElse(ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        Optional<ItemHandlerConfig> range = getRange(slot, StorageMode::canInput);
        return range.map(value -> value.getHandler().insertItem(value.getHandlerSlot(slot), stack, simulate)).orElse(stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Optional<ItemHandlerConfig> range = getRange(slot, StorageMode::canOutput);
        return range.map(value -> value.getHandler().extractItem(value.getHandlerSlot(slot), amount, simulate)).orElse(ItemStack.EMPTY);
    }

    @Override
    public int getSlotLimit(int slot) {
        Optional<ItemHandlerConfig> range = getRange(slot, mode -> true);
        return range.map(value -> value.getHandler().getSlotLimit(value.getHandlerSlot(slot))).orElse(0);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        Optional<ItemHandlerConfig> range = getRange(slot, mode -> true);
        return range.map(value -> value.getHandler().isItemValid(slot, stack)).orElse(false);
    }

}
