package io.github.ramboxeu.techworks.client.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;

public interface IExtendedContainerListener {
    void sendObjectUpdate(Container container, int index, CompoundNBT tag);
}
