package io.github.ramboxeu.techworks.common.component;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
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

            try {
                JsonObject obj = elem.getAsJsonObject();
                ResourceLocation typeId = new ResourceLocation(obj.get("type").getAsString());
                ComponentType<?> type = TechworksRegistries.COMPONENT_TYPES.getValue(typeId);

                if (type != null) {
                    Component component = type.read(id, obj);
                    builder.put(id, component);
                } else {
                    Techworks.LOGGER.error("Component {} uses un-registered type {}", id, typeId);
                }
            } catch (JsonParseException e) {
                Techworks.LOGGER.error("Couldn't read component {}, because it's json was invalid", id, e);
            } catch (RuntimeException e) {
                Techworks.LOGGER.error("Couldn't read component {}", id);
            }
        }

        components = builder.build();
        componentToItem = components.values().stream().collect(Collectors.toMap(Component::getItem, Function.identity()));
        itemComponentTag = Tag.getTagFromContents(components.values().stream().map(Component::getItem).collect(Collectors.toSet()));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public <T extends Component> T getComponent(ResourceLocation id) {
        T component = (T) components.get(id);

        if (component == null) {
            throw new RuntimeException("Component " + id + " does not exist.");
        }

        return component;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public <T extends Component> T getComponent(Item item) {
        T component =  (T) componentToItem.get(item);

        if (component == null) {
            throw new RuntimeException("Item " + item.delegate.name() + " does not map to any component.");
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
}
