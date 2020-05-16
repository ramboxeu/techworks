package io.github.ramboxeu.techworks.common.debug;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class DebuggerItem extends Item {
    private static final String[] TOOLTIPS = {"You are not spouse to use it!", "that's the worlds biggest secret", "ILLEGAL", "a.k.a bug RECTIFIER"};

    public DebuggerItem() {
        super(new Properties().maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TOOLTIPS[new Random().nextInt(TOOLTIPS.length)]));
    }
}
