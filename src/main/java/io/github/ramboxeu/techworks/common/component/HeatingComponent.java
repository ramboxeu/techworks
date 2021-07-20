package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.heat.HeaterType;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class HeatingComponent extends Component {
    private final HeaterType type;

    public HeatingComponent(ResourceLocation id, Item item, HeaterType type) {
        super(TechworksComponents.HEATING.get(), id, item);
        this.type = type;
    }

    public HeaterType getHeaterType() {
        return type;
    }

    public static class Type extends ComponentType<HeatingComponent> {
        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "solid_fuel_burner");

        @Override
        protected HeatingComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            String rawType = parameters.get("heater").getAsString().toUpperCase(Locale.ENGLISH);
            HeaterType type = HeaterType.valueOf(rawType);

            return new HeatingComponent(id, item, type);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
