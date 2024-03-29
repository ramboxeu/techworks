package io.github.ramboxeu.techworks.client.screen.widget;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.sync.ObjectHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IntReferenceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class BaseContainerWidget {
    public BaseContainerWidget() {
    }

    public void init(BaseContainer container, Builder builder) {}

    public void onButtonClicked(int buttonId) {}

    public static class Builder {
        private final List<Slot> slots = new ArrayList<>();
        private final List<ObjectHolder<?>> objectTrackers = new ArrayList<>();
        private final List<IntReferenceHolder> intTrackers = new ArrayList<>();
        private final List<BaseContainerWidget> subWidgets = new ArrayList<>();
        private final Function<Slot, BaseContainer.ToggleableSlot> wrapperMaker;

        public Builder(Function<Slot, BaseContainer.ToggleableSlot> wrapperMaker) {
            this.wrapperMaker = wrapperMaker;
        }

        public Builder slot(Slot slot) {
            slots.add(slot);
            return this;
        }

        public BaseContainer.ToggleableSlot toggleableSlot(Slot slot) {
            return wrapperMaker.apply(slot);
        }

        public Builder slots(Slot slot, Slot... slots) {
            this.slots.add(slot);
            this.slots.addAll(Arrays.asList(slots));
            return this;
        }

        public <T extends ObjectHolder<?>> T track(T object) {
            objectTrackers.add(object);
            return object;
        }

        public Builder track(IntReferenceHolder intTracker) {
            this.intTrackers.add(intTracker);
            return this;
        }

        public Builder track(IntReferenceHolder intTracker, IntReferenceHolder... intTrackers) {
            this.intTrackers.add(intTracker);
            this.intTrackers.addAll(Arrays.asList(intTrackers));
            return this;
        }

        public Builder subWidget(BaseContainerWidget widget) {
            subWidgets.add(widget);
            return this;
        }

        public List<Slot> getSlots() {
            return slots;
        }

        public List<ObjectHolder<?>> getObjectTrackers() {
            return objectTrackers;
        }

        public List<IntReferenceHolder> getIntTrackers() {
            return intTrackers;
        }

        public List<BaseContainerWidget> getSubWidgets() {
            return subWidgets;
        }
    }
}
