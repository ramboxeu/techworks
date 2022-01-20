package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public abstract class BaseStorageComponent extends Component {

    protected final int capacity;
    protected final int transfer;

    public BaseStorageComponent(BaseType<?> type, ResourceLocation id, Item item, int capacity, int transfer) {
        super(type, id, item);
        this.capacity = capacity;
        this.transfer = transfer;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTransfer() {
        return transfer;
    }

    public abstract int getStorageCapacity();
    public abstract int getStorageTransfer();

    public static abstract class BaseType<T extends BaseStorageComponent> extends ComponentType<T> {
        @Override
        protected T readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            int capacity = JsonUtils.readInt(parameters, "capacity");
            int transfer = JsonUtils.readInt(parameters, "transfer");

            return readComponent(id, item, parameters, capacity, transfer);
        }

        protected abstract T readComponent(ResourceLocation id, Item item, JsonObject parameters, int capacity, int transfer);
    }
}
