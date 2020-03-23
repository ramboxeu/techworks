package io.github.ramboxeu.techworks.api.gas;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

/*
Base gas class. This is by no means done and it's likely subject to change
@author Rambox
*/
public class Gas extends ForgeRegistryEntry<Gas> {
    private ResourceLocation texture;
    private String name;

    public Gas(ResourceLocation texture, String name) {
        this.texture = texture;
        this.name = name;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}
