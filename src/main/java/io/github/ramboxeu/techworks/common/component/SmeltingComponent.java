package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SmeltingComponent extends Component {
    private final float modifier;
    private final int bonus;
    private final int cap;

    public SmeltingComponent(ResourceLocation id, Item item, float modifier, int bonus, int cap) {
        super(TechworksComponents.SMELTING.get(), id, item);
        this.modifier = modifier;
        this.bonus = bonus;
        this.cap = cap;
    }

    @Override
    protected List<ITextComponent> collectTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>(3);
        int bonus = this.bonus * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        int cap = this.cap + bonus;

        if (bonus > 0) {
            tooltip.add(new TranslationTextComponent("tooltip.component.techworks.smelting_energy_bonus_cap", cap, bonus).setStyle(Component.TOOLTIP_STYLE));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.component.techworks.smelting_energy_cap", cap).setStyle(Component.TOOLTIP_STYLE));
        }
//        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.smelting_efficiency_bonus", bonus).setStyle(Component.TOOLTIP_STYLE));
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.smelting_energy_modifier", modifier).setStyle(Component.TOOLTIP_STYLE));

        return tooltip;
    }

    public float getModifier() {
        return modifier;
    }

    public int getBonus() {
        return bonus;
    }

    public int getCap(ItemStack stack) {
        return cap + (bonus * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack));
    }

    public static class Type extends ComponentType<SmeltingComponent> {

        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "electrified_furnace");

        @Override
        protected SmeltingComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            float modifier = parameters.get("modifier").getAsFloat();
            int energy = parameters.get("cap").getAsInt();
            int bonus = parameters.get("bonus").getAsInt();
            return new SmeltingComponent(id, item, modifier, bonus, energy);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
