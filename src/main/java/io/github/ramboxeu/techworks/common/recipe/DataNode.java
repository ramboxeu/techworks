package io.github.ramboxeu.techworks.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataNode implements IDataFunction, IDataNode {
    private final List<IDataFunction> children;
    private final String name;

    public DataNode(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    @Override
    public void accept(CompoundNBT nbt, Map<String, ItemStack> map) {
        CompoundNBT childNbt = new CompoundNBT();

        for (IDataFunction function : children) {
            function.accept(childNbt, map);
        }

        nbt.put(name, childNbt);
    }

    @Override
    public void addChild(IDataFunction child) {
        children.add(child);
    }
}
