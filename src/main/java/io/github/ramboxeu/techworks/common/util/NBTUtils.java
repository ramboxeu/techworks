package io.github.ramboxeu.techworks.common.util;

import com.google.gson.JsonPrimitive;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.UUID;

public class NBTUtils {

    @Nullable
    public static UUID getNetworkId(CompoundNBT tag) {
        if (tag.contains("NetworkId")) {
            return tag.getUniqueId("NetworkId");
        }

        return null;
    }

    public static <T extends Enum<T>> void serializeEnum(CompoundNBT nbt, String key, T value) {
        nbt.putString(key, value.name());
    }

    @Nullable
    public static <T extends Enum<T>> T deserializeEnum(CompoundNBT nbt, String key, Class<T> clazz) {
        try {
            return Enum.valueOf(clazz, nbt.getString(key));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static void serializeComponentType(CompoundNBT nbt, String key, ComponentType<?> type) {
        ResourceLocation id = TechworksRegistries.COMPONENT_TYPES.getKey(type);
        nbt.putString(key, id.toString());
    }

    @Nullable
    public static ComponentType<?> deserializeComponentType(CompoundNBT tag, String key) {
        String id = tag.getString(key);
        return TechworksRegistries.COMPONENT_TYPES.getValue(new ResourceLocation(id));
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
