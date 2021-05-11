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

public class EnergyStorageComponent extends Component {

    private final int storage;
    private final int maxInput;
    private final int maxOutput;

    public EnergyStorageComponent(ResourceLocation id, Item item, int storage, int maxInput, int maxOutput) {
        super(TechworksComponents.ENERGY_STORAGE.get(), id, item);
        this.storage = storage;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    public int getStorage() {
        return storage;
    }

    public int getMaxInput() {
        return maxInput;
    }

    public int getMaxOutput() {
        return maxOutput;
    }

    @Override
    protected List<ITextComponent> collectTooltip() {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.energy_capacity", storage).setStyle(Component.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.energy_max_input", maxInput).setStyle(Component.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.energy_max_output", maxOutput).setStyle(Component.TOOLTIP_STYLE));
        return tooltip;
    }

    public static class Type extends ComponentType<EnergyStorageComponent> {

        public static final ResourceLocation BASE_ID = new ResourceLocation(Techworks.MOD_ID, "base_energy_storage");

        @Override
        protected EnergyStorageComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            int storage = parameters.get("storage").getAsInt();
            int maxInput = parameters.get("maxInput").getAsInt();
            int maxOutput = parameters.get("maxOutput").getAsInt();

            return new EnergyStorageComponent(id, item, storage, maxInput, maxOutput);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE_ID;
        }
    }
}
