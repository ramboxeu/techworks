package io.github.ramboxeu.techworks.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;

import java.util.Locale;

public class JsonUtils {

    public static float readFloat(JsonObject obj, String key) {
        JsonElement element = getElement(obj, key);

        try {
            return element.getAsFloat();
        } catch (RuntimeException e) {
            throw new JsonParseException("Expected property '" + key + "' to have a float value.");
        }
    }

    public static int readInt(JsonObject obj, String key) {
        JsonElement element = getElement(obj, key);

        try {
            return element.getAsInt();
        } catch (RuntimeException e) {
            throw new JsonParseException("Expected property '" + key + "' to have a int value.");
        }
    }

    public static String readString(JsonObject obj, String key) {
        JsonElement element = getElement(obj, key);

        if (!element.isJsonPrimitive())
            throw new JsonParseException("Expected property '" + key + "' to have a string value.");

        return element.getAsString();
    }

    public static JsonObject readJsonObject(JsonObject obj, String key) {
        JsonElement element = getElement(obj, key);

        if (!element.isJsonObject())
            throw new JsonParseException("Expected property '" + key + "' to have a object value.");

        return element.getAsJsonObject();
    }

    public static <T extends Enum<T>> T readEnum(JsonObject obj, String key, Class<T> clazz) {
        String rawEnum = readString(obj, key).toUpperCase(Locale.ENGLISH);

        try {
            return Enum.valueOf(clazz, rawEnum);
        } catch (IllegalArgumentException e) {
            StringBuilder builder = new StringBuilder();
            builder.append("Property '").append(key).append("' value '").append(rawEnum).append("' could not be mapped to any enum constants. Valid values are: ");

            T[] constants = clazz.getEnumConstants();
            for (int i = 0; i < (constants.length - 1); i++) {
                builder.append(constants[i].name()).append(", ");
            }
            builder.append(constants[constants.length - 1].name());

            builder.append(" (case insensitive).");

            throw new JsonParseException(builder.toString());
        }
    }

    public static ResourceLocation readResourceLocation(JsonObject obj, String key) {
        String raw = readString(obj, key);

        try {
            return new ResourceLocation(raw);
        } catch (ResourceLocationException e) {
            throw new JsonParseException("Expected property '" + key + "' to have a valid resource location value: " + e.getMessage());
        }
    }

    private static JsonElement getElement(JsonObject obj, String key) {
        if (!obj.has(key))
            throw new JsonParseException("Required property '" + key + "' was not found.");

        return obj.get(key);
    }
}
