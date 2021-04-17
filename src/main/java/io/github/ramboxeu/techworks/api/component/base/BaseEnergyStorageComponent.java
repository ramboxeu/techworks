package io.github.ramboxeu.techworks.api.component.base;

import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.api.component.ComponentItem;
import io.github.ramboxeu.techworks.common.util.RenderUtils;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class BaseEnergyStorageComponent extends ComponentItem {
    private final int level;
    private final int capacity;

    public BaseEnergyStorageComponent(int level, int capacity) {
        super(new Item.Properties().maxStackSize(1).group(Techworks.ITEM_GROUP));

        this.level = level;
        this.capacity = capacity;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.techworks.energy_storage_component_description"));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_level", level));
        tooltip.add(new TranslationTextComponent("tooltip.techworks.component_energy_capacity", capacity));

        int energy = 0;

//        // Seems like you can't use capabilities when this first gets called,
//        // so to stop the game from crashing we need to void that exception
//        try {
//            LazyOptional<IEnergyStorage> capability = stack.getCapability(CapabilityEnergy.ENERGY);
//            if (capability.isPresent()) {
//                energy = capability.orElse(EmptyEnergyHandler.INSTANCE).getEnergyStored();
//            } else {
//                energy = 0;
//            }
//        } catch (NullPointerException ignored) {}

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
            IEnergyStorage storage = Utils.unpack(capability);
            return 1 - (storage.getEnergyStored() / (double) capacity);
        } else {
            return 1;
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return RenderUtils.rgbColor(190, 10, 15);
    }

    @Override
    public int getLevel() {
        return level;
    }

    public int getCapacity() {
        return capacity;
    }
}
