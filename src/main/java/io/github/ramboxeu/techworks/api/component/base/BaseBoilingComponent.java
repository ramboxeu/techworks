package io.github.ramboxeu.techworks.api.component.base;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BaseBoilingComponent extends ComponentItem {
    private final int level;
    private final int workTime;
    private final int steamAmount;
    private final int waterAmount;
    private final boolean unbreakable;

    public BaseBoilingComponent(int level, int maxDamage, int workTime, int waterAmount, int steamAmount) {
        super(new Properties().maxStackSize(1).maxDamage(maxDamage).group(Techworks.ITEM_GROUP));

        this.level = level;
        this.workTime = workTime;
        this.steamAmount = steamAmount;
        this.waterAmount = waterAmount;

        unbreakable = maxDamage < 0;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.techworks.boiling_component_description"));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_level", level));
        StringBuilder durability = new StringBuilder();

        if (!unbreakable) {
            int health = stack.getMaxDamage() - stack.getDamage();

            durability.append(
                    String.format("%d/%d (%d%%)",
                            health,
                            stack.getMaxDamage(),
                            (health / stack.getMaxDamage()) * 100
                    )
            );
        } else {
            durability.append(new TranslationTextComponent("tooltip.techworks.component_unbreakable"));
        }

        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_durability", durability.toString()));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getSteamAmount() {
        return steamAmount;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public int calcWorkTime() {
        return (workTime / (waterAmount / steamAmount));
    }
}
