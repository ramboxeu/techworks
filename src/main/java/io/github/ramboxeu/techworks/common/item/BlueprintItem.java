package io.github.ramboxeu.techworks.common.item;

import io.github.ramboxeu.techworks.common.registration.TechworksItems;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlueprintItem extends Item {
    private static final Style TOOLTIP_STYLE = Style.EMPTY.setColor(Color.func_240744_a_(TextFormatting.GOLD));

    private final Item machineItem;
    private final ResourceLocation recipeId;

    //recipeId should match machine this blueprint is tied to, so machine registry id can be directly converted to recipe
    public BlueprintItem(Properties properties, Block machineBlock, ResourceLocation recipeId) {
        super(properties.maxStackSize(1));

        machineItem = machineBlock.asItem();
        this.recipeId = recipeId;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("item.techworks.blueprint");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this == TechworksItems.BOILER_BLUEPRINT.getItem()) {
            tooltip.add(makeTooltipEntry("boiler"));
        } else if (this == TechworksItems.STEAM_ENGINE_BLUEPRINT.getItem()) {
            tooltip.add(makeTooltipEntry("steam_engine"));
        } else if (this == TechworksItems.ELECTRIC_FURNACE_BLUEPRINT.getItem()) {
            tooltip.add(makeTooltipEntry("electric_furnace"));
        } else if (this == TechworksItems.ELECTRIC_GRINDER_BLUEPRINT.getItem()) {
            tooltip.add(makeTooltipEntry("electric_grinder"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public Item getMachine() {
        return machineItem;
    }

    public ResourceLocation getRecipeId() {
        return recipeId;
    }

    private static ITextComponent makeTooltipEntry(String type) {
        return new TranslationTextComponent("tooltip.techworks.blueprint." + type).func_230530_a_(TOOLTIP_STYLE);
    }
}
