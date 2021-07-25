package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.recipe.GrinderRecipeType;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
            tooltip.add(TranslationKeys.BONUS_ENERGY_CAP.styledText(Component.TOOLTIP_STYLE, cap, bonus));
        } else {
            tooltip.add(TranslationKeys.ENERGY_CAP.styledText(Component.TOOLTIP_STYLE, cap));
        }

        tooltip.add(TranslationKeys.ENERGY_MODIFIER.styledText(Component.TOOLTIP_STYLE, modifier));

        if (type == GrinderRecipeType.ORE_CRUSHING) {
            tooltip.add(TranslationKeys.ORE_CRUSHER_NOTE.styledText(Component.TOOLTIP_STYLE));
        }

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
            GrinderRecipeType type = JsonUtils.readEnum(parameters, "recipe", GrinderRecipeType.class);
            float modifier = JsonUtils.readFloat(parameters, "modifier");
            int energy = JsonUtils.readInt(parameters, "cap");
            int bonus = JsonUtils.readInt(parameters, "bonus");

            return new GrindingComponent(id, item, type, modifier, energy, bonus);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
