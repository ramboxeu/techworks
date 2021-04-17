package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public final class ContainerRegistryObject<T extends Container> {
    private final RegistryObject<ContainerType<T>> container;

    ContainerRegistryObject(RegistryObject<ContainerType<T>> container) {
        this.container = container;
    }

    public ContainerType<T> get() {
        return container.get();
    }

    public ResourceLocation getId() {
        return container.getId();
    }
}
