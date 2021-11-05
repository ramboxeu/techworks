package io.github.ramboxeu.techworks.common.util;

import com.google.gson.JsonPrimitive;
import io.github.ramboxeu.techworks.common.component.Component;
import io.github.ramboxeu.techworks.common.component.ComponentManager;
import io.github.ramboxeu.techworks.common.component.ComponentType;
import io.github.ramboxeu.techworks.common.registration.TechworksRegistries;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

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
        if (value == null) {
            nbt.putBoolean(key, false);
        } else {
            nbt.putString(key, value.name());
        }
    }

    @Nullable
    public static <T extends Enum<T>> T deserializeEnum(CompoundNBT nbt, String key, Class<T> clazz) {
        try {
            if (nbt.getTagId(key) == Constants.NBT.TAG_BYTE) {
                return null;
            } else {
                return Enum.valueOf(clazz, nbt.getString(key));
            }
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

    @Nullable
    public static Component deserializeComponent(CompoundNBT tag, String key) {
        ResourceLocation id = new ResourceLocation(tag.getString(key));
        return ComponentManager.getInstance().getComponent(id);
    }

    public static void serializeComponent(CompoundNBT tag, String key, Component component) {
        tag.putString(key, component.getId().toString());
    }

    @Nullable
    public static BlockPos getBlockPos(CompoundNBT tag, String key) {
        if (tag.contains(key, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT posTag = tag.getCompound(key);
            int x = posTag.getInt("x");
            int y = posTag.getInt("y");
            int z = posTag.getInt("z");

            return new BlockPos(x, y, z);
        }

        return null;
    }

    public static void putBlockPos(CompoundNBT tag, String key, BlockPos pos) {
        CompoundNBT posTag = new CompoundNBT();
        posTag.putInt("x", pos.getX());
        posTag.putInt("y", pos.getY());
        posTag.putInt("z", pos.getZ());

        tag.put(key, posTag);
    }
}
