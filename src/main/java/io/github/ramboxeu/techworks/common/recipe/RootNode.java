package io.github.ramboxeu.techworks.common.recipe;

import io.github.ramboxeu.techworks.Techworks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RootNode implements IDataFunction, IDataNode {
    private final List<IDataFunction> children;

    public RootNode() {
        children = new ArrayList<>();
    }

    @Override
    public void accept(CompoundNBT nbt, Map<String, ItemStack> map) {
        for (IDataFunction function : children) {
            function.accept(nbt, map);
        }
    }

    @Override
    public void addChild(IDataFunction child) {
        children.add(child);
    }
}
