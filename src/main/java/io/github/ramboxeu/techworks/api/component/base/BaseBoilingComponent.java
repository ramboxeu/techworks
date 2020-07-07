package io.github.ramboxeu.techworks.api.component.base;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BaseBoilingComponent extends ComponentItem {
    private int level;

    public BaseBoilingComponent(int level, int maxDamage) {
        super(new Properties().maxStackSize(1).maxDamage(maxDamage).group(Techworks.ITEM_GROUP));

        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        // Change this with translate-able version
        tooltip.add(new StringTextComponent("Boils water. Core component of boiler"));
        tooltip.add(new StringTextComponent("Level: " + level));
        tooltip.add(new StringTextComponent(String.format("Durability: %d/%d (%d%%)", stack.getDamage(), stack.getMaxDamage(), ((stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage()) * 100 )));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
