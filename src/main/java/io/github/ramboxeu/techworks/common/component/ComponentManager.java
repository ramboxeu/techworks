package io.github.ramboxeu.techworks.common.component;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComponentManager extends JsonReloadListener {

    private static ComponentManager INSTANCE;

    private static final String DIR_NAME = "components";
    private static final Gson GSON = new GsonBuilder().create();

    private Map<ResourceLocation, Component> components;
    private Map<Item, Component> componentToItem;
    private Tag<Item> itemComponentTag;

    public ComponentManager() {
        super(GSON, DIR_NAME);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager manager, IProfiler profiler) {
        ImmutableMap.Builder<ResourceLocation, Component> builder = new ImmutableMap.Builder<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement elem = entry.getValue();

            if (!elem.isJsonObject())
                throw new ComponentManagerException("Expected component's %s JSON file root element to be object.", id);

            JsonObject obj = elem.getAsJsonObject();
            ResourceLocation typeId = JsonUtils.readResourceLocation(obj, "type");
            ComponentType<?> type = TechworksRegistries.COMPONENT_TYPES.getValue(typeId);

            if (type != null) {
                try {
                    Component component = type.read(id, obj);
                    builder.put(id, component);
                } catch (JsonParseException e) {
                    throw new ComponentManagerException("Component's %s JSON file is invalid: %s", id, e.getMessage());
                } catch (ComponentReadException e) {
                    throw new ComponentManagerException("Component %s is invalid: %s.", id, e.getMessage());
                } catch (Exception e) {
                    throw new ComponentManagerException("Unexpected error while reading component " + id + ".", e);
                }
            } else {
                throw new ComponentManagerException("Component %s uses un-registered type %s.", id, typeId);
            }
        }

        components = builder.build();
        componentToItem = components.values().stream().collect(Collectors.toMap(Component::getItem, Function.identity(), ComponentManager::helpfulThrowingMerger));
        itemComponentTag = Tag.getTagFromContents(components.values().stream().map(Component::getItem).collect(Collectors.toSet()));
    }

    private static Component helpfulThrowingMerger(Component a, Component b) {
        throw new ComponentManagerException("Item %s can be used by only one component, but multiple uses were found: %s, %s.", a.getItem().delegate.name(), a.getId(), b.getId());
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public <T extends Component> T getComponent(ResourceLocation id) {
        T component = (T) components.get(id);

        if (component == null) {
            throw new ComponentManagerException("Component " + id + " does not exist.");
        }

        return component;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public <T extends Component> T getComponent(Item item) {
        T component =  (T) componentToItem.get(item);

        if (component == null) {
            throw new ComponentManagerException("Item " + item.delegate.name() + " does not map to any component.");
        }

        return component;
    }

    public ITag<Item> getItemComponentTag() {
        return itemComponentTag;
    }

    public static ComponentManager getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Can not retrieve ComponentManager until resources have loaded at least once.");

        return INSTANCE;
    }

    public static IFutureReloadListener createListener() {
        INSTANCE = new ComponentManager();
        return INSTANCE;
    }

    public boolean isItemComponent(Item item) {
        return componentToItem.containsKey(item);
    }

    private static class ComponentManagerException extends RuntimeException {

        public ComponentManagerException(String message, Throwable cause) {
            super(message, cause);
        }

        public ComponentManagerException(String format, Object... args) {
            super(String.format(format, args));
        }
    }
}
