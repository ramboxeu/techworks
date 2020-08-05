package io.github.ramboxeu.techworks.api.component.base;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import io.github.ramboxeu.techworks.common.capability.UniversalProvider;
import io.github.ramboxeu.techworks.common.util.RenderUtils;
import io.github.ramboxeu.techworks.common.util.capability.EmptyEnergyHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class BaseEnergyStorageComponent extends ComponentItem {
    private final int level;
    private final int maxEnergy;

    public BaseEnergyStorageComponent(int level, int maxEnergy) {
        super(new Item.Properties().maxStackSize(1).group(Techworks.ITEM_GROUP));

        this.level = level;
        this.maxEnergy = maxEnergy;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.techworks.energy_storage_component_description"));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_level", level));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_energy_capacity", maxEnergy));

        int energy = 0;

        // Seems like you can't use capabilities when this first gets called,
        // so to stop the game from crashing we need to void that exception
        try {
            LazyOptional<IEnergyStorage> capability = stack.getCapability(CapabilityEnergy.ENERGY);
            if (capability.isPresent()) {
                energy = capability.orElse(EmptyEnergyHandler.INSTANCE).getEnergyStored();
            } else {
                energy = 0;
            }
        } catch (NullPointerException ignored) {}

        tooltip.add(new TranslationTextComponent("tooltip.techworks.energy_storage_component_energy", energy));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        LazyOptional<IEnergyStorage> capability = stack.getCapability(CapabilityEnergy.ENERGY);

        if (capability.isPresent()) {
            IEnergyStorage storage = capability.orElse(EmptyEnergyHandler.INSTANCE);
            return 1 - (storage.getEnergyStored() / (double) maxEnergy);
        } else {
            return 1;
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return RenderUtils.rgbColor(190, 10, 15);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (this.getClass() == BaseEnergyStorageComponent.class) {
            return new UniversalProvider<>(CapabilityEnergy.ENERGY, new EnergyStorage(maxEnergy));
        } else {
            return super.initCapabilities(stack, nbt);
        }
    }

    @Override
    public int getLevel() {
        return level;
    }
}
