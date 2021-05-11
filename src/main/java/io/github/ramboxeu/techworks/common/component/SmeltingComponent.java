package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SmeltingComponent extends Component {
    private final float energy;
    private final float time;

    public SmeltingComponent(ResourceLocation id, Item item, float energy, float time) {
        super(TechworksComponents.SMELTING.get(), id, item);
        this.energy = energy;
        this.time = time;
    }

    @Override
    protected List<ITextComponent> collectTooltip() {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.smelting_time_modifier", time).setStyle(Component.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.smelting_energy_modifier", energy).setStyle(Component.TOOLTIP_STYLE));
        return tooltip;
    }

    public float getEnergy() {
        return energy;
    }

    public float getTime() {
        return time;
    }

    public static class Type extends ComponentType<SmeltingComponent> {

        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "base_smelting");

        @Override
        protected SmeltingComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            float energy = parameters.get("energy").getAsFloat();
            float time = parameters.get("time").getAsFloat();
            return new SmeltingComponent(id, item, energy, time);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
