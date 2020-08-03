package io.github.ramboxeu.techworks.common.registry;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class ContainerRegistryObject<CONTAINER extends Container> {
    private final RegistryObject<ContainerType<CONTAINER>> containerRegistryObject;

    public ContainerRegistryObject(RegistryObject<ContainerType<CONTAINER>> containerRegistryObject) {
        this.containerRegistryObject = containerRegistryObject;
    }

    public ContainerType<CONTAINER> getContainer() {
        return containerRegistryObject.get();
    }

    public ResourceLocation getRegistryName() {
        return containerRegistryObject.getId();
    }
}
