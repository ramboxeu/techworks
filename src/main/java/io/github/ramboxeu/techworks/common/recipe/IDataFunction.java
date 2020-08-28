package io.github.ramboxeu.techworks.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.Map;

public interface IDataFunction {
    void accept(CompoundNBT nbt, Map<String, ItemStack> map);
}
