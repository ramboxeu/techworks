package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ComponentType<T extends Component> extends ForgeRegistryEntry<ComponentType<?>> {

    public T read(ResourceLocation id, JsonObject json) {
        ResourceLocation itemId = new ResourceLocation(json.get("item").getAsString());
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        JsonObject params = json.getAsJsonObject("parameters");

        return readComponent(id, item, params);
    }

    protected abstract T readComponent(ResourceLocation id, Item item, JsonObject parameters);

    public abstract ResourceLocation getBaseComponentId();

    public abstract String getName();
}
