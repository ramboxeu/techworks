package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.recipe.GrinderRecipeType;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class GrindingComponent extends Component {
    private final GrinderRecipeType type;
    private final float modifier;
    private final int bonus;
    private final int cap;

    public GrindingComponent(ResourceLocation id, Item item, GrinderRecipeType type, float modifier, int bonus, int cap) {
        super(TechworksComponents.GRINDING.get(), id, item);
        this.type = type;
        this.modifier = modifier;
        this.bonus = bonus;
        this.cap = cap;
    }

    @Override
    protected List<ITextComponent> collectTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        int bonus = this.bonus * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        int cap = this.cap + bonus;

        if (bonus > 0) {
            tooltip.add(new TranslationTextComponent("tooltip.component.techworks.grinding_energy_bonus_cap", cap, bonus).setStyle(Component.TOOLTIP_STYLE));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.component.techworks.grinding_energy_cap", cap).setStyle(Component.TOOLTIP_STYLE));
        }
        tooltip.add(new TranslationTextComponent("tooltip.component.techworks.grinding_energy_modifier", modifier).setStyle(Component.TOOLTIP_STYLE));

        return tooltip;
    }

    public GrinderRecipeType getRecipeType() {
        return type;
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

    public static class Type extends ComponentType<GrindingComponent> {

        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "rock_crusher");

        @Override
        protected GrindingComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            GrinderRecipeType type = JsonUtils.helpfulReadEnum(parameters, "recipe", GrinderRecipeType.class);
            float modifier = parameters.get("modifier").getAsFloat();
            int energy = parameters.get("cap").getAsInt();
            int bonus = parameters.get("bonus").getAsInt();
            return new GrindingComponent(id, item, type, modifier, energy, bonus);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
