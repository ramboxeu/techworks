package io.github.ramboxeu.techworks.common.util;

import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class NBTUtils {

    @Nullable
    public static UUID getNetworkId(CompoundNBT tag) {
        if (tag.contains("NetworkId")) {
            return tag.getUniqueId("NetworkId");
        }

        return null;
    }

    public static <T extends Enum<T>> Optional<T> deserializeEnum(CompoundNBT nbt, String key, Class<T> clazz) {
        try {
            return Optional.of(Enum.valueOf(clazz, nbt.getString(key)));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    public static <T extends Enum<T>> void serializeEnum(CompoundNBT nbt, String key, T value) {
        nbt.putString(key, value.name());
    }

    @Nullable
    public static INBT getNbtFromJson(JsonPrimitive primitive) {
        if (primitive.isString()) {
            return StringNBT.valueOf(primitive.getAsString());
        } else if (primitive.isBoolean()) {
            return ByteNBT.valueOf(primitive.getAsBoolean());
        } else if (primitive.isNumber()) {
            try {
                int number = primitive.getAsInt();
                return IntNBT.valueOf(number);
            } catch (NumberFormatException e) {
                try {
                    double number = primitive.getAsDouble();
                    return DoubleNBT.valueOf(number);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return null;
    }

}
