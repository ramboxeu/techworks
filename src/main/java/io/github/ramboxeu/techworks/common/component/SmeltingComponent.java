package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class SmeltingComponent extends Component {
    private final int energy;
    private final int time;

    public SmeltingComponent(ResourceLocation id, Item item, int energy, int time) {
        super(TechworksComponents.SMELTING.get(), id, item);
        this.energy = energy;
        this.time = time;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    public static class Type extends ComponentType<SmeltingComponent> {

        @Override
        protected SmeltingComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            int energy = parameters.get("energy").getAsInt();
            int time = parameters.get("time").getAsInt();
            return new SmeltingComponent(id, item, energy, time);
        }

        @Override
        public String getName() {
            return "Smelting Component";
        }
    }
}
