package io.github.ramboxeu.techworks.common.component;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComponentManager extends JsonReloadListener {

    public static ComponentManager INSTANCE;

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
                Component component = type.read(id, obj);
                builder.put(id, component);
            } catch (RuntimeException e) {
                Techworks.LOGGER.error("Couldn't read component {}", id);
            }
        }

        components = builder.build();
        componentToItem = components.values().stream().collect(Collectors.toMap(Component::getItem, Function.identity()));
        itemComponentTag = Tag.getTagFromContents(components.values().stream().map(Component::getItem).collect(Collectors.toSet()));
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(ResourceLocation id) {
        return (T) components.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Item item) {
        return (T) componentToItem.get(item);
    }

    public ITag<Item> getItemComponentTag() {
        return itemComponentTag;
    }

    public static ComponentManager getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Can not retrieve ComponentManager until resources have loaded once.");

        return INSTANCE;
    }
}
