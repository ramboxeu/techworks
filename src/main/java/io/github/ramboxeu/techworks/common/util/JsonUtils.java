package io.github.ramboxeu.techworks.common.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.Locale;

public class JsonUtils {
    public static <T extends Enum<T>> T helpfulReadEnum(JsonObject obj, String key, Class<T> clazz) {
        String rawEnum = obj.get(key).getAsString().toUpperCase(Locale.ENGLISH);

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
}
