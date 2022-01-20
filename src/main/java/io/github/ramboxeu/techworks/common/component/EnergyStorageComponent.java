package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class EnergyStorageComponent extends BaseStorageComponent {

    public EnergyStorageComponent(ResourceLocation id, Item item, int capacity, int transfer) {
        super(TechworksComponents.ENERGY_STORAGE.get(), id, item, capacity, transfer);
    }

    @Override
    public List<ITextComponent> collectTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        tooltip.add(TranslationKeys.ENERGY_STORAGE_CAPACITY.styledText(Component.TOOLTIP_STYLE, capacity));
        tooltip.add(TranslationKeys.ENERGY_TRANSFER_RATE.styledText(Component.TOOLTIP_STYLE, transfer));
        return tooltip;
    }

    @Override
    public int getStorageCapacity() {
        return capacity * 50;
    }

    @Override
    public int getStorageTransfer() {
        return transfer * 5;
    }

    public static class Type extends BaseType<EnergyStorageComponent> {

        public static final ResourceLocation BASE_ID = new ResourceLocation(Techworks.MOD_ID, "small_battery");

        @Override
        protected EnergyStorageComponent readComponent(ResourceLocation id, Item item, JsonObject parameters, int capacity, int transfer) {
            return new EnergyStorageComponent(id, item, capacity, transfer);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE_ID;
        }
    }
}
