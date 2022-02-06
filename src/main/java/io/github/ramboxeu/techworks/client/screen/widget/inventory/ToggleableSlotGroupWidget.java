package io.github.ramboxeu.techworks.client.screen.widget.inventory;

import io.github.ramboxeu.techworks.client.container.BaseContainer;
import io.github.ramboxeu.techworks.client.container.BaseMachineContainer;
import io.github.ramboxeu.techworks.common.util.machineio.data.ItemHandlerData;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ToggleableSlotGroupWidget extends SlotGroupWidget {

    private final List<BaseContainer.ToggleableSlot> slots;
    private boolean enabled = true;

    public ToggleableSlotGroupWidget(BaseMachineContainer<?> container, int x, int y, int rows, int cols, ItemHandlerData data) {
        this(container, x, y, rows, cols, data, SlotItemHandler::new);
    }

    public ToggleableSlotGroupWidget(BaseMachineContainer<?> container, int x, int y, int rows, int cols, ItemHandlerData data, SlotFactory factory) {
        super(container, x, y, rows, cols, data, factory);
        slots = new ArrayList<>(rows * cols);
    }

    @Override
    public void init(BaseContainer container, Builder builder) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = this.x + 1 + (j * 18);
                int y = this.y + 1 + (i * 18);

                BaseContainer.ToggleableSlot slot = builder.toggleableSlot(factory.create(handler, (i * cols) + j, x, y));
                slots.add(slot);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        setState(enabled, false);
    }

    public void setState(boolean enabled, boolean sync) {
        if (this.enabled == enabled)
            return;

        this.enabled = enabled;

        for (BaseContainer.ToggleableSlot slot : slots) {
            slot.setState(enabled, sync);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
