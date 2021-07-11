package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class EnergyStorageComponent extends Component {

    private final int capacity;
    private final int transfer;

    public EnergyStorageComponent(ResourceLocation id, Item item, int capacity, int transfer) {
        super(TechworksComponents.ENERGY_STORAGE.get(), id, item);
        this.capacity = capacity;
        this.transfer = transfer;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTransfer() {
        return transfer;
    }

    @Override
    protected List<ITextComponent> collectTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.energy_capacity", capacity).setStyle(Component.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.energy_transfer_rate", transfer).setStyle(Component.TOOLTIP_STYLE));
        return tooltip;
    }

    public static class Type extends ComponentType<EnergyStorageComponent> {

        public static final ResourceLocation BASE_ID = new ResourceLocation(Techworks.MOD_ID, "small_battery");

        @Override
        protected EnergyStorageComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            int capacity = parameters.get("capacity").getAsInt();
            int transfer = parameters.get("transfer").getAsInt();

            return new EnergyStorageComponent(id, item, capacity, transfer);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE_ID;
        }
    }
}
