package io.github.ramboxeu.techworks.common.gas;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

/*
Base gas class. This is by no means done and it's likely subject to change

This is heavily inspired by Mekanism (see https://github.com/mekanism/Mekansim)

@author Rambox
*/
public class Gas extends ForgeRegistryEntry<Gas> {
    private Properties properties;

    public Gas(Properties properties) {
        this.properties = properties;
    }

    public boolean shouldRender() {
        return properties.shouldRender;
    }

    public boolean isHidded() {
        return properties.hidden;
    }

    public ResourceLocation getTexture() {
        return properties.texture;
    }

    public static class Properties {
        private boolean hidden;           // Does it show in JEI
        private ResourceLocation texture; // Gas texture, might be null
        private boolean shouldRender;

        public Properties hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public Properties texture(ResourceLocation texture) {
            this.texture = texture;
            this.shouldRender = true;
            return this;
        }
    }
}
