package io.github.ramboxeu.techworks.common.component;

import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.lang.TranslationKeys;
import io.github.ramboxeu.techworks.common.registration.TechworksComponents;
import io.github.ramboxeu.techworks.common.util.JsonUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class MiningComponent extends Component {
    private final int energyUsage;
    private final int miningTime;

    public MiningComponent(ResourceLocation id, Item item, int energyUsage, int miningTime) {
        super(TechworksComponents.MINING.get(), id, item);
        this.energyUsage = energyUsage;
        this.miningTime = miningTime;
    }

    @Override
    public List<ITextComponent> getTooltipInfo(ItemStack stack) {
        return getTooltipInfo();
    }

    @Override
    protected List<ITextComponent> collectTooltip(ItemStack stack) {
        List<ITextComponent> tooltip = new ArrayList<>(2);
        tooltip.add(TranslationKeys.ENERGY_USAGE.styledText(Component.TOOLTIP_STYLE, energyUsage));
        tooltip.add(TranslationKeys.MINING_TIME.styledText(Component.TOOLTIP_STYLE, miningTime));
        return tooltip;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    public int getMiningTime() {
        return miningTime;
    }

    public static class Type extends ComponentType<MiningComponent> {
        private static final ResourceLocation BASE = new ResourceLocation(Techworks.MOD_ID, "electric_drill");

        @Override
        protected MiningComponent readComponent(ResourceLocation id, Item item, JsonObject parameters) {
            int energy = JsonUtils.readInt(parameters, "energy");
            int time = JsonUtils.readInt(parameters, "energy");

            return new MiningComponent(id, item, energy, time);
        }

        @Override
        public ResourceLocation getBaseComponentId() {
            return BASE;
        }
    }
}
