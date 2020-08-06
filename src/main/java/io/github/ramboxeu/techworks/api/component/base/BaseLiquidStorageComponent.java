package io.github.ramboxeu.techworks.api.component.base;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import io.github.ramboxeu.techworks.common.util.RenderUtils;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BaseLiquidStorageComponent extends ComponentItem {
    private final int level;
    private final int volume;

    public BaseLiquidStorageComponent(int level, int volume) {
        super(new Properties().maxStackSize(1).group(Techworks.ITEM_GROUP));

        this.level = level;
        this.volume = volume;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public int getVolume() {
        return volume;
    }

    @SuppressWarnings({"ConstantConditions", "CatchMayIgnoreException"})
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        // Change this with translate-able version
        tooltip.add(new TranslationTextComponent("tooltip.techworks.liquid_storage_component_description"));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_level", level));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_capacity", volume));
        String fluidName = new TranslationTextComponent("fluid.techworks.none").getString();

        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();

            if (nbt.contains("FluidTank", Constants.NBT.TAG_COMPOUND)) {
                FluidStack fluid = Utils.getFluidFromNBT(nbt.getCompound("FluidTank"));

                if (!fluid.isEmpty()) {
                    fluidName = fluid.getDisplayName().getString();
                } else {
                    fluidName = new TranslationTextComponent("fluid.techworks.empty").getString();
                }
            }
        }

        tooltip.add(new TranslationTextComponent("tooltip.techworks.liquid_storage_component_liquid", fluidName));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("FluidTank", Constants.NBT.TAG_COMPOUND)) {
            FluidStack fluid = Utils.getFluidFromNBT(stack.getTag().getCompound("FluidTank"));
            return 1 - (fluid.getAmount() / (double) volume);
        }

        return 1;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return RenderUtils.rgbColor(0, 91, 204);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
