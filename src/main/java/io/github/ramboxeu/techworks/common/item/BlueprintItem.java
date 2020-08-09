package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlueprintItem extends Item {
    private static final Style TOOLTIP_STYLE = Style.EMPTY.setColor(Color.func_240744_a_(TextFormatting.GOLD));

    private final Item machineItem;

    public BlueprintItem(Properties properties, Block machineBlock) {
        super(properties);

        machineItem = machineBlock.asItem();
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("item.techworks.blueprint");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this == TechworksItems.BOILER_BLUEPRINT.getItem()) {
            tooltip.add(makeTooltipEntry("boiler"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public Item getMachine() {
        return machineItem;
    }

    private static ITextComponent makeTooltipEntry(String type) {
        return new TranslationTextComponent("tooltip.techworks.blueprint." + type).func_230530_a_(TOOLTIP_STYLE);
    }
}
