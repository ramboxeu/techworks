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

public class GasStorageComponent extends BaseStorageComponent {

    public GasStorageComponent(ResourceLocation id, Item item, int capacity, int transfer) {
        super(TechworksComponents.GAS_STORAGE.get(), id, item, capacity, transfer);
    }

    @Override
    public List<ITextComponent> collectTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.fluid_storage_capacity", capacity));
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.fluid_storage_transfer_rate", transfer));
        return tooltip;
    }

    public static class Type extends BaseType<GasStorageComponent> {

        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "small_gas_tank");

        @Override
        protected GasStorageComponent readComponent(ResourceLocation id, Item item, JsonObject parameters, int capacity, int transfer) {
            return new GasStorageComponent(id, item, capacity, transfer);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
