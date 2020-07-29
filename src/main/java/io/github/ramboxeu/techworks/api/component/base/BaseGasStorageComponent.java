package io.github.ramboxeu.techworks.api.component.base;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import io.github.ramboxeu.techworks.common.util.RenderUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BaseGasStorageComponent extends ComponentItem {
    private int level;
    private int volume;

    public BaseGasStorageComponent(int level, int volume) {
        super(new Properties().group(Techworks.ITEM_GROUP).maxStackSize(1));
        this.level = level;
        this.volume = volume;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.techworks.gas_storage_component_description"));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_level", level));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_capacity", volume));

        String fluidName = new TranslationTextComponent("fluid.techworks.none").getString();

        // Seems like you can't use capabilities when this first gets called,
        // so to stop the game from crashing we need to void that exception
        try {
            LazyOptional<IFluidHandlerItem> capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
            if (capability.isPresent()) {
                FluidStack fluidStack = capability.orElse(null).getFluidInTank(0);
                if (fluidStack.isEmpty()) {
                    fluidName = new TranslationTextComponent("fluid.techworks.empty").getString();
                } else {
                    fluidName = fluidStack.getFluid().getAttributes().getDisplayName(fluidStack).getString();
                }
            }
        } catch (NullPointerException e) {}

        tooltip.add(new TranslationTextComponent("tooltip.techworks.gas_storage_component_gas", fluidName));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        LazyOptional<IFluidHandlerItem> capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (capability.isPresent()) {
            FluidStack fluid = capability.orElse(null).getFluidInTank(0);
            return 1 - (fluid.getAmount() / (double) volume);
        } else {
            return 1;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        LazyOptional<IFluidHandlerItem> capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (capability.isPresent()) {
            FluidStack fluid = capability.orElse(null).getFluidInTank(0);
            return fluid.getFluid().getAttributes().getColor();
        } else {
            return RenderUtils.rgbColor(198, 201, 204);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (this.getClass() == BaseGasStorageComponent.class) {
            return new FluidHandlerItemStack(stack, volume) {
                @Override
                public boolean canFillFluidType(FluidStack fluid) {
                    return fluid.getFluid().getAttributes().isGaseous(fluid);
                }

                @Override
                public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                    return canFillFluidType(stack);
                }
            };
        } else {
            return super.initCapabilities(stack, nbt);
        }
    }
}
