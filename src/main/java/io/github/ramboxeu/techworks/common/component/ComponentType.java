package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ComponentType<T extends Component> extends ForgeRegistryEntry<ComponentType<?>> {

    private String translationKey;

    public T read(ResourceLocation id, JsonObject json) {
        ResourceLocation itemId = new ResourceLocation(json.get("item").getAsString());
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        JsonObject params = json.getAsJsonObject("parameters");

        return readComponent(id, item, params);
    }

    protected abstract T readComponent(ResourceLocation id, Item item, JsonObject parameters);

    public abstract ResourceLocation getBaseComponentId();

    public ITextComponent getName() {
        return new TranslationTextComponent(getTranslationKey());
    }

    public String getTranslationKey() {
        return getDefaultTranslationKey();
    }

    private String getDefaultTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.makeTranslationKey("component_type", TechworksRegistries.COMPONENT_TYPES.getKey(this));
        }

        return translationKey;
    }
}
