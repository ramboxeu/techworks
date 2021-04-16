package io.github.ramboxeu.techworks.client.util;

import net.minecraft.util.math.vector.Vector4f;

public class TextureCoords {
    public final float minU;
    public final float minV;
    public final float maxU;
    public final float maxV;
    public final Vector4f vector;

    public TextureCoords(float minU, float minV, float maxU, float maxV) {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;

        vector = new Vector4f(minU, minV, maxU, maxV);
    }

    public static TextureCoords make(float minU, float minV, float maxU, float maxV) {
        return new TextureCoords(minU, minV, maxU, maxV);
    }
}
