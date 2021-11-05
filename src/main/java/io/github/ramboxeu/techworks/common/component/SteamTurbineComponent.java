package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class SteamTurbineComponent extends Component {
    private final int maxEnergy;
    private final float efficiency;

    public SteamTurbineComponent(ResourceLocation id, Item item, int maxEnergy, float efficiency) {
        super(TechworksComponents.STEAM_TURBINE.get(), id, item);
        this.maxEnergy = maxEnergy;
        this.efficiency = efficiency;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public static class Type extends ComponentType<SteamTurbineComponent> {
        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "steam_turbine_mk1");

        @Override
        protected SteamTurbineComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            int maxEnergy = JsonUtils.readInt(parameters, "maxEnergy");
            float efficiency = JsonUtils.readFloat(parameters, "efficiency");

            return new SteamTurbineComponent(id, item, maxEnergy, efficiency);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
