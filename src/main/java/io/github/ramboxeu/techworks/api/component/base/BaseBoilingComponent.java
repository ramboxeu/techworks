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
    private final int level;
    private final int cookTime;
    private final int amount;
    private final boolean unbreakable;

    @SuppressWarnings("ConstantConditions")
    public BaseBoilingComponent(int level, int maxDamage, boolean hidden, int cookTime, int amount) {
        super(new Properties().maxStackSize(1).maxDamage(maxDamage).group(!hidden ? Techworks.ITEM_GROUP : null));

        this.level = level;
        this.cookTime = cookTime;
        this.amount = amount;

        unbreakable = maxDamage < 0;
    }

    public BaseBoilingComponent(int level, int maxDamage, int cookTime, int amount) {
        this(level, maxDamage, false, cookTime, amount);
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
        StringBuilder durability = new StringBuilder("Durability: ");

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
            durability.append("Unbreakable");
        }

        tooltip.add(new StringTextComponent(durability.toString()));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getAmountProduced() {
        return amount;
    }
}
