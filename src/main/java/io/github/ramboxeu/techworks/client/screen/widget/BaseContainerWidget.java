package io.github.ramboxeu.techworks.client.screen.widget;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.ObjectReferenceHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IntReferenceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseContainerWidget {
    public BaseContainerWidget() {
    }

    public void init(BaseContainer container, Builder builder) {}

    public void onButtonClicked(int buttonId) {}

    public static class Builder {
        private final List<Slot> slots = new ArrayList<>();
        private final List<ObjectReferenceHolder> objectTrackers = new ArrayList<>();
        private final List<IntReferenceHolder> intTrackers = new ArrayList<>();

        public Builder slot(Slot slot) {
            slots.add(slot);
            return this;
        }

        public Builder slots(Slot slot, Slot... slots) {
            this.slots.add(slot);
            this.slots.addAll(Arrays.asList(slots));
            return this;
        }

        public Builder track(ObjectReferenceHolder objectTracker) {
            objectTrackers.add(objectTracker);
            return this;
        }

        public Builder track(ObjectReferenceHolder objectTracker, ObjectReferenceHolder... objectTrackers) {
            this.objectTrackers.add(objectTracker);
            this.objectTrackers.addAll(Arrays.asList(objectTrackers));
            return this;
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

        public List<Slot> getSlots() {
            return slots;
        }

        public List<ObjectReferenceHolder> getObjectTrackers() {
            return objectTrackers;
        }

        public List<IntReferenceHolder> getIntTrackers() {
            return intTrackers;
        }
    }
}
