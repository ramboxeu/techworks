package io.github.ramboxeu.techworks.client.model;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.client.model.gas.BasicGasPipeModelGeometry;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class CableModelLoader implements IModelLoader<BasicGasPipeModelGeometry> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public BasicGasPipeModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        switch (modelContents.get("type").getAsString()) {
            case "gas":
                switch (modelContents.get("tier").getAsString()) {
                    case "basic": return new BasicGasPipeModelGeometry();
                    default:
                        throw new IllegalStateException("Unknown pipe tier");
                }
            default:
                throw new IllegalStateException("Unknown cable type");
        }
    }
}
