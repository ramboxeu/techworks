package io.github.ramboxeu.techworks.client.model.cable;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class CableModelLoader implements IModelLoader<CableModel> {
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public CableModel read(JsonDeserializationContext context, JsonObject model) {
        return new CableModel();
    }
}
