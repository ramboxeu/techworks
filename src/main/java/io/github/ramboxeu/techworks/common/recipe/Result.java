package io.github.ramboxeu.techworks.common.recipe;

import com.google.gson.*;
import io.github.ramboxeu.techworks.Techworks;
import io.github.ramboxeu.techworks.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private final ItemStack rawOutput;
    private final RootNode dataRoot;

    public Result(ItemStack rawOutput, RootNode dataRoot) {
        this.rawOutput = rawOutput;
        this.dataRoot = dataRoot;
    }

    public ItemStack getRawOutput() {
        return rawOutput;
    }

    // Safe to modify afterwards
    public ItemStack craft(Map<String, ItemStack> map) {
        if (dataRoot == null || map == null) {
            return rawOutput.copy();
        }

        ItemStack stack = rawOutput.copy();
        CompoundNBT itemNbt = stack.getOrCreateTag();
        dataRoot.accept(itemNbt, map);
        return stack;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeItemStack(rawOutput);
    }

    public static Result read(PacketBuffer buffer) {
        return new Result(buffer.readItemStack(), null);
    }

    public static Result deserialize(JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                ItemStack stack = CraftingHelper.getItemStack(object, true);

                if (object.has("data")) {
                    if (object.get("data").isJsonObject()) {
                        RootNode root = new RootNode();

                        for (Map.Entry<String, JsonElement> entry : object.get("data").getAsJsonObject().entrySet()) {
                            parseData(entry.getValue(), entry.getKey(), root);
                        }

                        return new Result(stack, root);
                    } else {
                        throw new JsonSyntaxException("Invalid result: expected 'data' to be object");
                    }
                }

                return new Result(stack, null);
            } else {
                throw new JsonSyntaxException("Invalid result: expected object");
            }
        } else {
            throw new JsonSyntaxException("Invalid result: object cannot be null");
        }
    }

    private static void parseData(JsonElement json, String name, IDataNode parent) {
        if (json.isJsonPrimitive()) {
            JsonPrimitive value = json.getAsJsonPrimitive();

            if (value.isString()) {
                parent.addChild((nbt, map) -> nbt.putString(name, value.getAsString()));
            } else if (value.isBoolean()) {
                parent.addChild((nbt, map) -> nbt.putBoolean(name, value.getAsBoolean()));
            } else if (value.isNumber()) {
                try {
                    int number = value.getAsInt();
                    parent.addChild((nbt, map) -> nbt.putInt(name, number));
                } catch(NumberFormatException a) {
                    try {
                        double number = value.getAsDouble();
                        parent.addChild((nbt, map) -> nbt.putDouble(name, number));
                    } catch (NumberFormatException b) {
                        Techworks.LOGGER.warn("Number '{}' is not a valid integer or double, ignoring", name);
                    }
                }
            }
        } else if (json.isJsonArray()) {
            Techworks.LOGGER.info("Json arrays are not supported yet");
        } else if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();

            if (object.has("function")) {
                String function = object.get("function").getAsJsonPrimitive().getAsString();

                if (function.equals("copy")) {
                    String key;
                    String path;

                    if (object.has("key")) {
                        key = object.get("key").getAsJsonPrimitive().getAsString();

                        if (key.length() != 1) {
                            throw new JsonSyntaxException("Function 'copy' exception: invalid key, length is not 1");
                        }
                    } else {
                        throw new JsonSyntaxException("Function 'copy' exception: expected 'key', but it was not found");
                    }

                    if (object.has("path")) {
                        path = object.get("path").getAsJsonPrimitive().getAsString();
                    } else {
                        throw new JsonSyntaxException("Function 'copy' exception: expected 'path', but it was not found");
                    }

                    parent.addChild((nbt, map) -> {
                        ItemStack stack = map.get(key);

                        if (stack != null) {
                            if (stack.hasTag()) {
                                INBT tag = stack.getTag().get(path);
                                if (tag != null) {
                                    nbt.put(name, tag);
                                } else {
                                    Techworks.LOGGER.warn("Copy function: path '{}' (from item '{}') is null", path, key);
                                }
                            } else {
                                Techworks.LOGGER.warn("Copy function: item under key '{}' doesn't have a tag", key);
                            }
                        } else {
                            Techworks.LOGGER.warn("Copy function: item under key '{}' is null", key);
                        }
                    });
                } else if (function.equals("switch")) {
                    String key;

                    if (object.has("key")) {
                        key = object.get("key").getAsJsonPrimitive().getAsString();

                        if (key.length() != 1) {
                            throw new JsonSyntaxException("Function 'switch' exception: invalid key, length is not 1");
                        }
                    } else {
                        throw new JsonSyntaxException("Function 'switch' exception: expected 'key', but it was not found");
                    }

                    HashMap<String, INBT> switchMap = new HashMap<>();
                    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                        if (entry.getKey().equals("function") || entry.getKey().equals("key")) {
                            continue;
                        }

                        switchMap.put(entry.getKey(), Utils.getNbtFromJson(entry.getValue().getAsJsonPrimitive()));
                    }

                    parent.addChild((nbt, map) -> {
                        ItemStack stack = map.get(key);

                        if (stack != null) {
                            INBT value = switchMap.get(stack.getItem().getRegistryName().toString());

                            if (value != null) {
                                nbt.put(name, value);
                            } else {
                                Techworks.LOGGER.warn("Switch function: key '{}' returned null", stack.getItem().getRegistryName());
                            }
                        } else {
                            Techworks.LOGGER.warn("Switch function: item under key '{}' is null", key);
                        }
                    });
                } else {
                    throw new JsonSyntaxException("Function exception: unknown function '" + function + "'");
                }
            } else {
                DataNode child = new DataNode(name);
                parent.addChild(child);
                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    parseData(entry.getValue(), entry.getKey(), child);
                }
            }
        }
    }
}
